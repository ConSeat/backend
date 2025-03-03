package site.concertseat.global.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import site.concertseat.domain.member.entity.Role;
import site.concertseat.global.jwt.service.JwtUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String uuid = oAuth2User.getUuid();
        Role role = oAuth2User.getRole();
        String userAgent = request.getHeader("User-Agent");

        String accessToken = jwtUtils.createAccessToken(uuid, role);
        String refreshToken = jwtUtils.createRefreshToken(uuid, userAgent);

        // todo - 토큰 전송 방식은 수정 예정
        response.setHeader("Authorization", accessToken);
        response.addCookie(new Cookie("refresh", refreshToken));
    }
}
