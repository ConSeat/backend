package site.concertseat.domain.member.service;

import site.concertseat.domain.member.dto.res.MemberSearchRes;
import site.concertseat.domain.member.entity.Member;

public interface MemberService {
    MemberSearchRes searchMember(Member member);
}
