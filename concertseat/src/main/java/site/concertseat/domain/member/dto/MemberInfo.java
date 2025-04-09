package site.concertseat.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberInfo {
    private String nickname;

    private String profileImage;

    private String email;
}
