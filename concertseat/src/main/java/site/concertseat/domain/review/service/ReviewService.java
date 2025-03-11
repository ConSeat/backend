package site.concertseat.domain.review.service;

import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.dto.ReviewPostReq;
import site.concertseat.domain.review.dto.ReviewSearchRes;

public interface ReviewService {
    void postReview(Member member, Integer lineId, Integer concertId, ReviewPostReq reviewPostReq);

    ReviewSearchRes searchReview(Member member, Integer seatingId);
}
