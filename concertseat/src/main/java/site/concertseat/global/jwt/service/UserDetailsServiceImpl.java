package site.concertseat.global.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.member.repository.MemberRepository;
import site.concertseat.global.jwt.entity.UserDetailsImpl;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) {

        Optional<Member> member = memberRepository.findByUuid(memberId);

        return member.map(value ->
                new UserDetailsImpl(value, Collections.singletonList(value.getRole()))).orElse(null);
    }
}
