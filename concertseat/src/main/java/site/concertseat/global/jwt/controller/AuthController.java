package site.concertseat.global.jwt.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.concertseat.global.dto.ResponseDto;
import site.concertseat.global.jwt.dto.RefreshDto;
import site.concertseat.global.jwt.service.JwtUtils;
import site.concertseat.global.statuscode.SuccessCode;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;

    public ResponseDto<Void> refresh(@RequestHeader("Authorization") String token,
                                     @RequestHeader("User-Agent") String userAgent,
                                     HttpServletResponse response) {
        RefreshDto refresh = jwtUtils.refresh(token, userAgent);

        response.setHeader("Authorization", refresh.getAccessToken());
        response.addCookie(new Cookie("refresh", refresh.getRefreshToken()));

        return ResponseDto.success(SuccessCode.OK);
    }
}
