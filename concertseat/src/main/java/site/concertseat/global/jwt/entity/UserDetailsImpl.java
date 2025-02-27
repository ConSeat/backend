package site.concertseat.global.jwt.entity;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import site.concertseat.domain.member.entity.Member;

import java.util.Collection;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private Member member;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return member.getUuid();
    }
}
