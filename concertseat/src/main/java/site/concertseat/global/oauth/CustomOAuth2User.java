package site.concertseat.global.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.member.enums.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private Member member;

    private Map<String, Object> attributes;

    public CustomOAuth2User(Member member, OAuth2User oAuth2User) {
        this.member = member;
        this.attributes = oAuth2User.getAttributes();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(member.getRole());
    }

    @Override
    public String getName() {
        return member.getNickname();
    }

    public String getUuid() {
        return member.getUuid();
    }

    public Role getRole() {
        return member.getRole();
    }
}
