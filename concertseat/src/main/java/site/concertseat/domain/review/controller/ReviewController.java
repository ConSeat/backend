package site.concertseat.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.dto.ImageUploadRes;
import site.concertseat.domain.review.dto.ReviewPostReq;
import site.concertseat.domain.review.dto.ReviewSearchRes;
import site.concertseat.domain.review.service.ReviewService;
import site.concertseat.global.argument_resolver.LoginMember;
import site.concertseat.global.dto.ResponseDto;
import site.concertseat.global.jwt.service.JwtUtils;
import site.concertseat.global.s3.S3Service;

import java.io.IOException;
import java.util.List;

import static site.concertseat.global.statuscode.SuccessCode.CREATED;
import static site.concertseat.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final S3Service s3Service;
    private final JwtUtils jwtUtils;

    @GetMapping("/seating/{seatingId}")
    public ResponseDto<ReviewSearchRes> reviewSearch(@LoginMember Member member,
                                                     @PathVariable Integer seatingId) {
        ReviewSearchRes res = reviewService.searchReview(member, seatingId);

        return ResponseDto.success(OK, res);
    }

    @PostMapping("/concerts/{concertId}/seating/{seatingId}")
    public ResponseDto<Void> reviewPost(@LoginMember Member member,
                                        @PathVariable Integer seatingId,
                                        @PathVariable Integer concertId,
                                        @Valid @RequestBody ReviewPostReq reviewPostReq) {
        reviewService.postReview(member, seatingId, concertId, reviewPostReq);

        return ResponseDto.success(CREATED);
    }

    @PostMapping("/images")
    public ResponseDto<ImageUploadRes> imageUpload(@LoginMember Member member,
                                                   @RequestParam("files") List<MultipartFile> files) throws IOException {
        ImageUploadRes res = reviewService.uploadImage(member, files);

        return ResponseDto.success(OK, res);
    }
}
