package site.concertseat.domain.admin.service;

import org.springframework.data.domain.Pageable;
import site.concertseat.domain.admin.dto.req.AdminReviewListReq;
import site.concertseat.domain.admin.dto.req.ChangeReviewStatusReq;
import site.concertseat.domain.admin.dto.res.AdminReviewDetails;
import site.concertseat.domain.admin.dto.res.AdminReviewListRes;

public interface AdminService {
    AdminReviewListRes findAdminReviews(Pageable pageable, AdminReviewListReq adminReviewListReq);

    AdminReviewDetails findAdminReview(Long reviewId);

    void changeReviewStatus(Long reviewId, ChangeReviewStatusReq request);

    void deleteReview(Long reviewId);
}
