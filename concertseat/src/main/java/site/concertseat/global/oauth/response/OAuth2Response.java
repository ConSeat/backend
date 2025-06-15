package site.concertseat.global.oauth.response;

import site.concertseat.domain.member.entity.Member;
import site.concertseat.global.util.NicknameUtils;

import java.util.UUID;

import static site.concertseat.domain.member.enums.Role.ROLE_USER;

public interface OAuth2Response {
    String getProvider();

    String getSocialId();

    String getNickname();

    default Member toEntity() {
        return Member.builder()
                .uuid(UUID.randomUUID().toString())
                .socialId(getSocialId())
                .nickname(NicknameUtils.createRandomNickname())
                .src("https://conseat.s3.ap-northeast-2.amazonaws.com/profile/default.png")
                .role(ROLE_USER)
                .providerType(getProvider())
                .build();
    }
}
