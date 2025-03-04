package site.concertseat.domain.member.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN
    ;

    @Override
    public String getAuthority() {
        return ROLE_USER.name();
    }
}
