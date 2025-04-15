package site.concertseat.domain.admin.service;

import site.concertseat.domain.admin.dto.req.ApproveReviewReq;
import site.concertseat.domain.member.entity.Member;

public interface AdminService {
    void approveReview(Member member, Long reviewId, ApproveReviewReq request);
}
