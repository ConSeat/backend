package site.concertseat.domain.admin.service;

import org.springframework.data.domain.Pageable;
import site.concertseat.domain.admin.dto.req.AdminReviewListReq;
import site.concertseat.domain.admin.dto.req.ApproveReviewReq;
import site.concertseat.domain.admin.dto.res.AdminReviewDetails;
import site.concertseat.domain.admin.dto.res.AdminReviewListRes;
import site.concertseat.domain.member.entity.Member;

public interface AdminService {
    AdminReviewListRes findAdminReviews(Pageable pageable, AdminReviewListReq adminReviewListReq);

    AdminReviewDetails findAdminReview(Long reviewId);

    void approveReview(Member member, Long reviewId, ApproveReviewReq request);
}
