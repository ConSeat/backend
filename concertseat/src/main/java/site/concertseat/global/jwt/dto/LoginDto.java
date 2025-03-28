package site.concertseat.global.jwt.dto;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {
    private String accessToken;

    private Cookie cookie;
}
