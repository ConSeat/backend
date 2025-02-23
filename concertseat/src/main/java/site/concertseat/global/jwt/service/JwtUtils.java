package site.concertseat.global.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.member.entity.Role;
import site.concertseat.domain.member.repository.MemberRepository;
import site.concertseat.global.exception.CustomException;
import site.concertseat.global.jwt.dto.RefreshDto;
import site.concertseat.global.redis.RedisUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static site.concertseat.global.statuscode.ErrorCode.INVALID_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access_expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh_expiration}")
    private long refreshExpiration;

    private Key key;

    private final RedisUtils redisUtils;
    private final MemberRepository memberRepository;

    private static final String ISSUER = "ISS";
    private static final String ACCESS_TOKEN_CLAIM_NAME = "access_token";
    private static final String REFRESH_TOKEN_CLAIM_NAME = "refresh_token";

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String createAccessToken(String uuid, Role role) {
        Map<String, Object>  claims = new HashMap<>();
        claims.put("type", ACCESS_TOKEN_CLAIM_NAME);
        claims.put("role", role.getAuthority());

        return createToken(uuid, Jwts.claims(claims), accessExpiration);
    }

    public String createRefreshToken(String uuid, String userAgent) {
        Map<String, Object>  claims = new HashMap<>();
        claims.put("type", REFRESH_TOKEN_CLAIM_NAME);

        String token = createToken(uuid, Jwts.claims(claims), refreshExpiration);
        String deviceId = getDeviceIdFromUserAgent(userAgent);

        redisUtils.setDataWithExpiration("refresh:" + uuid + ":" + deviceId, token, refreshExpiration);

        return token;
    }

    public String createToken(String uuid, Claims claims, long expirationTime) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime * 1000);

        return Jwts.builder()
                .setSubject(uuid)
                .setClaims(claims)
                .setExpiration(validity)
                .setIssuedAt(now)
                .setIssuer(ISSUER)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUuid(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            if (isExpired(token)) {
                return false;
            }

            String issuer = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getIssuer();

            return issuer.equals(ISSUER);
        } catch (Exception e) {
            return false;
        }
    }

    public RefreshDto refresh(String token, String userAgent) {
        validateRefreshToken(token, userAgent);

        String uuid = getUuid(token);

        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new CustomException(INVALID_TOKEN));

        String accessToken = createAccessToken(uuid, member.getRole());
        String refreshToken = createRefreshToken(uuid, userAgent);

        return new RefreshDto(accessToken, refreshToken);
    }

    private void validateRefreshToken(String token, String userAgent) throws CustomException {
        if (!validateToken(token) || !isExpired(token) || !isRefreshToken(token)) {
            throw new CustomException(INVALID_TOKEN);
        }

        String deviceId = getDeviceIdFromUserAgent(userAgent);
        String refreshToken = (String) redisUtils.getData("refresh:" + token + ":" + deviceId);

        if (refreshToken == null || !refreshToken.equals(token)) {
            throw new CustomException(INVALID_TOKEN);
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public boolean isExpired(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }

    public boolean isAccessToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .get("Type")
                .equals(ACCESS_TOKEN_CLAIM_NAME);
    }

    public boolean isRefreshToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .get("Type")
                .equals(REFRESH_TOKEN_CLAIM_NAME);
    }

    private String getDeviceIdFromUserAgent(String userAgent) {
        return DigestUtils.sha256Hex(userAgent);
    }
}
