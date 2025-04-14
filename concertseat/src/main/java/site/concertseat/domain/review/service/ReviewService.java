package site.concertseat.domain.review.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.dto.req.MyReviewSearchReq;
import site.concertseat.domain.review.dto.req.ReviewListReq;
import site.concertseat.domain.review.dto.req.ReviewPostReq;
import site.concertseat.domain.review.dto.res.*;

import java.io.IOException;
import java.util.List;

public interface ReviewService {
    void postReview(Member member, Integer lineId, Integer concertId, ReviewPostReq reviewPostReq);

    ReviewSearchRes searchReview(Member member, Integer seatingId);

    ReviewListRes findReviews(Member member, Integer seatingId, ReviewListReq reviewListReq, Pageable pageable);

    ImageUploadRes uploadImage(Member member, List<MultipartFile> file) throws IOException;

    Long getTotalReviewCount();

    MyReviewStadiumListRes findStadium(Member member);

    MyReviewSearchRes searchMyReview(Member member, MyReviewSearchReq req, Pageable pageable);

    MyReviewDetailRes myReviewDetails(Member member, Long reviewId);
}
