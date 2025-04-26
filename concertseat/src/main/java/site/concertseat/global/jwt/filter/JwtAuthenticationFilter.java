package site.concertseat.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import site.concertseat.global.dto.ResponseData;
import site.concertseat.global.dto.ResponseHeader;
import site.concertseat.global.jwt.service.JwtUtils;

import java.io.IOException;

import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_TOKEN;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtUtils tokenProvider;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean isTokenValid = false;

        String token = tokenProvider.resolveToken(request);

        if (tokenProvider.validateAccessToken(token)) {
            String uuid = tokenProvider.getUuid(token, true);
            UserDetails userDetails = userDetailsService.loadUserByUsername(uuid);

            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                isTokenValid = true;
            }
        }

        if (!isTokenValid) {
            sendInvalidTokenError(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendInvalidTokenError(HttpServletResponse response) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ResponseData res = new ResponseData(new ResponseHeader(INVALID_TOKEN), null);
        response.getWriter().write(new ObjectMapper().writeValueAsString(res));
    }
}
