package site.concertseat.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import site.concertseat.domain.admin.dto.req.AdminReviewListReq;
import site.concertseat.domain.admin.dto.req.ChangeReviewStatusReq;
import site.concertseat.domain.admin.dto.res.AdminReviewDetails;
import site.concertseat.domain.admin.dto.res.AdminReviewListRes;
import site.concertseat.domain.admin.service.AdminService;
import site.concertseat.global.dto.ResponseDto;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static site.concertseat.global.statuscode.SuccessCode.NO_CONTENT;
import static site.concertseat.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/reviews")
    public ResponseDto<AdminReviewListRes> adminReviewList(@ModelAttribute AdminReviewListReq adminReviewListReq,
                                                           @PageableDefault(direction = DESC) Pageable pageable) {
        AdminReviewListRes result = adminService.findAdminReviews(pageable, adminReviewListReq);

        return ResponseDto.success(OK, result);
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseDto<AdminReviewDetails> findAdminReview(@PathVariable Long reviewId) {
        AdminReviewDetails result = adminService.findAdminReview(reviewId);

        return ResponseDto.success(OK, result);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseDto<Void> changeReviewStatus(@PathVariable Long reviewId,
                                                @RequestBody ChangeReviewStatusReq request) {
        adminService.changeReviewStatus(reviewId, request);

        return ResponseDto.success(NO_CONTENT);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseDto<Void> deleteReview(@PathVariable Long reviewId) {
        adminService.deleteReview(reviewId);

        return ResponseDto.success(NO_CONTENT);
    }
}
