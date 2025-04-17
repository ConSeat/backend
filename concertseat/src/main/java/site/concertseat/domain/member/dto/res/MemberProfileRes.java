package site.concertseat.domain.member.dto.res;

import lombok.Data;
import site.concertseat.domain.member.dto.MemberInfo;

@Data
public class MemberProfileRes {
    private String nickname;

    private String profileImage;

    private String email;

    public MemberProfileRes(MemberInfo memberInfo) {
        this.nickname = memberInfo.getNickname();
        this.profileImage = memberInfo.getProfileImage();
        this.email = memberInfo.getEmail();
    }
}
