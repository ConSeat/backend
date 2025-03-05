package site.concertseat.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.dto.ReviewPostReq;
import site.concertseat.domain.review.service.ReviewService;
import site.concertseat.global.argument_resolver.LoginMember;
import site.concertseat.global.dto.ResponseDto;

import static site.concertseat.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/seating/{seatingId}/concerts/{concertId}")
    public ResponseDto<Void> reviewPost(@LoginMember Member member,
                                                 @PathVariable Integer seatingId,
                                                 @PathVariable Integer concertId,
                                                 @Valid @RequestBody ReviewPostReq reviewPostReq) {
        reviewService.postReview(member, seatingId, concertId, reviewPostReq);

        return ResponseDto.success(OK);
    }
}
