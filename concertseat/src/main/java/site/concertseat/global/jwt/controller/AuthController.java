package site.concertseat.global.jwt.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.concertseat.global.dto.ResponseDto;
import site.concertseat.global.jwt.dto.LoginDto;
import site.concertseat.global.jwt.service.JwtUtils;

import static site.concertseat.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseDto<Void> login(@RequestHeader("User-Agent") String userAgent,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        LoginDto loginDto = jwtUtils.login(userAgent, request);

        response.setHeader("Authorization", loginDto.getAccessToken());
        response.addCookie(loginDto.getCookie());

        return ResponseDto.success(OK);
    }
}
