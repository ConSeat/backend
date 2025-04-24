package site.concertseat.global.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import site.concertseat.global.jwt.service.JwtUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String uuid = oAuth2User.getUuid();
        String userAgent = request.getHeader("User-Agent");

        response.addCookie(jwtUtils.createRefreshCookie(uuid, userAgent));

        switch (oAuth2User.getRole()) {
            case ROLE_USER -> response.sendRedirect("/callback");
            case ROLE_ADMIN -> response.sendRedirect("/admin/callback");
        }
    }
}
