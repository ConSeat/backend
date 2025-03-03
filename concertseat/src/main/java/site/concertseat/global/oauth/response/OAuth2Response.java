package site.concertseat.global.oauth.response;

import site.concertseat.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.UUID;

import static site.concertseat.domain.member.entity.Role.ROLE_USER;

public interface OAuth2Response {
    String getProvider();

    String getSocialId();

    String getNickname();

    default Member toEntity() {
        return Member.builder()
                .uuid(UUID.randomUUID().toString())
                .socialId(getSocialId())
                .nickname(getNickname())
                .src("defaultImage")
                .role(ROLE_USER)
                .providerType(getProvider())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }
}
