package site.concertseat.domain.review.service;

import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.dto.ReviewPostReq;

public interface ReviewService {
    void postReview(Member member, Integer lineId, Integer concertId, ReviewPostReq reviewPostReq);
}
