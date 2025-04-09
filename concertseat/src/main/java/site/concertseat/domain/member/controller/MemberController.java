package site.concertseat.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.concertseat.domain.member.dto.res.MemberSearchRes;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.member.service.MemberService;
import site.concertseat.global.argument_resolver.LoginMember;
import site.concertseat.global.dto.ResponseDto;

import static site.concertseat.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseDto<MemberSearchRes> memberSearch(@LoginMember Member member) {
        MemberSearchRes res = memberService.searchMember(member);

        return ResponseDto.success(OK, res);
    }
}
