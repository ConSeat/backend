package site.concertseat.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.concertseat.domain.admin.dto.req.ApproveReviewReq;
import site.concertseat.domain.admin.service.AdminService;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.global.argument_resolver.LoginMember;
import site.concertseat.global.dto.ResponseDto;

import static site.concertseat.global.statuscode.SuccessCode.NO_CONTENT;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PatchMapping("/reviews/{reviewId}/approval")
    public ResponseDto<Void> approveReview(@LoginMember Member member,
                                           @PathVariable Long reviewId,
                                           @RequestBody ApproveReviewReq request) {
        adminService.approveReview(member, reviewId, request);

        return ResponseDto.success(NO_CONTENT);
    }
}
