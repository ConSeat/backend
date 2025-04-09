package site.concertseat.domain.review.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import site.concertseat.domain.review.dto.ReviewDto;
import site.concertseat.domain.review.dto.ReviewListReq;

public interface CustomReviewRepository {
    Slice<ReviewDto> findReviews(ReviewListReq reviewListReq, Integer seatingId,Pageable pageable);
}
