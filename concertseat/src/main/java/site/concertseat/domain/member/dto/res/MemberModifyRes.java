package site.concertseat.domain.member.dto.res;

import lombok.Data;

@Data
public class MemberModifyRes {
    private String nickname;

    private String profileImage;

    public MemberModifyRes(String nickname, String imageSrc) {
        this.nickname = nickname;
        this.profileImage = imageSrc;
    }
}
