package site.concertseat.global.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshDto {
    private String accessToken;

    private String refreshToken;
}
