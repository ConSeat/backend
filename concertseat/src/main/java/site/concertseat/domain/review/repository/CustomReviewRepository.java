package site.concertseat.domain.review.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import site.concertseat.domain.review.dto.MyReviewDto;
import site.concertseat.domain.review.dto.ReviewDto;
import site.concertseat.domain.review.dto.req.MyReviewSearchReq;
import site.concertseat.domain.review.dto.req.ReviewListReq;

public interface CustomReviewRepository {
    Slice<ReviewDto> findReviews(ReviewListReq reviewListReq, Integer seatingId, Pageable pageable);

    Long countReviews(Integer seatingId, ReviewListReq reviewListReq);

    Slice<MyReviewDto> findMyReviews(Long memberId, MyReviewSearchReq req, Pageable pageable);
}
