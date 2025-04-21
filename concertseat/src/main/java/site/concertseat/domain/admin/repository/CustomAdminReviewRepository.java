package site.concertseat.domain.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.concertseat.domain.admin.dto.AdminReviewDto;
import site.concertseat.domain.admin.dto.req.AdminReviewListReq;

public interface CustomAdminReviewRepository {
    Page<AdminReviewDto> findAdminReviews(Pageable pageable, AdminReviewListReq adminReviewListReq);
}
