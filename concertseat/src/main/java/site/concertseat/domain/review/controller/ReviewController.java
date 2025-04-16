package site.concertseat.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.concertseat.domain.bookmark.service.BookmarkService;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.dto.req.MyReviewSearchReq;
import site.concertseat.domain.review.dto.req.ReviewListReq;
import site.concertseat.domain.review.dto.req.ReviewPostReq;
import site.concertseat.domain.review.dto.res.*;
import site.concertseat.domain.review.service.ReviewService;
import site.concertseat.global.argument_resolver.LoginMember;
import site.concertseat.global.dto.ResponseDto;

import java.io.IOException;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static site.concertseat.global.statuscode.SuccessCode.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final BookmarkService bookmarkService;

    @GetMapping("/seating/{seatingId}")
    public ResponseDto<ReviewSearchRes> reviewSearch(@LoginMember Member member,
                                                     @PathVariable Integer seatingId) {
        ReviewSearchRes res = reviewService.searchReview(member, seatingId);

        return ResponseDto.success(OK, res);
    }

    @GetMapping("/seating/{seatingId}/list")
    public ResponseDto<ReviewListRes> reviewList(@LoginMember Member member,
                                                 @PathVariable Integer seatingId,
                                                 @ModelAttribute ReviewListReq reviewListReq,
                                                 @PageableDefault(size = 3, sort = "likesCount", direction = DESC)
                                                     Pageable pageable) {
        ReviewListRes result = reviewService.findReviews(member, seatingId, reviewListReq, pageable);

        return ResponseDto.success(OK, result);
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

        return ResponseDto.success(CREATED, res);
    }

    @GetMapping("/stadiums")
    public ResponseDto<MyReviewStadiumListRes> myReviewStadiumList(@LoginMember Member member) {
        MyReviewStadiumListRes res = reviewService.findStadium(member);

        return ResponseDto.success(OK, res);
    }

    @GetMapping
    public ResponseDto<MyReviewSearchRes> myReviewSearch(@LoginMember Member member,
                                                                     @Valid @ModelAttribute MyReviewSearchReq req,
                                                                     @PageableDefault(size = 9) Pageable pageable) {
        MyReviewSearchRes res = reviewService.searchMyReview(member, req, pageable);

        return ResponseDto.success(OK, res);
    }

    @PostMapping("/{reviewId}/bookmarks")
    public ResponseDto<Void> addBookmark(@LoginMember Member member,
                                         @PathVariable Long reviewId) {
        bookmarkService.addBookmark(member, reviewId);

        return ResponseDto.success(CREATED);
    }

    @DeleteMapping("/{reviewId}/bookmarks")
    public ResponseDto<Void> deleteBookmark(@LoginMember Member member,
                                            @PathVariable Long reviewId) {
        bookmarkService.deleteBookmark(member, reviewId);

        return ResponseDto.success(DELETED);
    }

    @PostMapping("/{reviewId}/likes")
    public ResponseDto<Void> addLike(@LoginMember Member member,
                                     @PathVariable Long reviewId) {
        reviewService.addLike(member, reviewId);

        return ResponseDto.success(CREATED);
    }

    @DeleteMapping("/{reviewId}/likes")
    public ResponseDto<Void> deleteLike(@LoginMember Member member,
                                        @PathVariable Long reviewId) {
        reviewService.deleteLike(member, reviewId);

        return ResponseDto.success(NO_CONTENT);
    }

    @GetMapping("/{reviewId}")
    public ResponseDto<MyReviewDetailRes> myReviewDetail(@LoginMember Member member,
                                                         @PathVariable Long reviewId) {
        MyReviewDetailRes res = reviewService.myReviewDetails(member, reviewId);

        return ResponseDto.success(OK, res);
    }
}
