package site.concertseat.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import site.concertseat.domain.member.dto.req.MemberModifyReq;
import site.concertseat.domain.member.dto.res.MemberProfileRes;
import site.concertseat.domain.member.dto.res.MemberSearchRes;
import site.concertseat.domain.member.dto.res.MemberModifyRes;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.member.service.MemberService;
import site.concertseat.global.argument_resolver.LoginMember;
import site.concertseat.global.dto.ResponseDto;

import java.io.IOException;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<MemberModifyRes> memberModify(@LoginMember Member member,
                                                      @Valid @ModelAttribute MemberModifyReq memberModifyReq) {
        MemberModifyRes res = memberService.modifyMember(member, memberModifyReq);

        return ResponseDto.success(OK, res);
    }

    @GetMapping("/profile")
    public ResponseDto<MemberProfileRes> memberProfile(@LoginMember Member member) {
        MemberProfileRes res = memberService.getMemberInfo(member);

        return ResponseDto.success(OK, res);
    }
}
