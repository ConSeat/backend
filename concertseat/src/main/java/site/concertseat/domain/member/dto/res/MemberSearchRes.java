package site.concertseat.domain.member.dto.res;

import lombok.Data;
import site.concertseat.domain.member.dto.MemberInfo;

@Data
public class MemberSearchRes {
    private String nickname;

    private String profileImage;

    private String email;

    private Long favoriteCount;

    private Long myReviewCount;

    public MemberSearchRes(MemberInfo memberInfo, Long favoriteCount, Long myReviewCount) {
        this.nickname = memberInfo.getNickname();
        this.profileImage = memberInfo.getProfileImage();
        this.email = memberInfo.getEmail();
        this.favoriteCount = favoriteCount;
        this.myReviewCount = myReviewCount;
    }
}
