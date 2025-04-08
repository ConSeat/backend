package site.concertseat.domain.member.service;

import site.concertseat.domain.member.dto.req.MemberModifyReq;
import site.concertseat.domain.member.dto.res.MemberSearchRes;
import site.concertseat.domain.member.dto.res.MemberModifyRes;
import site.concertseat.domain.member.entity.Member;

import java.io.IOException;

public interface MemberService {
    MemberSearchRes searchMember(Member member);

    MemberModifyRes modifyMember(Member member, MemberModifyReq memberModifyReq) throws IOException;
}
