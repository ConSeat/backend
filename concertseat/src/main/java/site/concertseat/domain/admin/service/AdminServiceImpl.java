package site.concertseat.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.util.EnumUtils;
import site.concertseat.domain.admin.dto.req.ApproveReviewReq;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.entity.Review;
import site.concertseat.domain.review.enums.ReviewStatus;
import site.concertseat.domain.review.repository.ReviewRepository;
import site.concertseat.global.exception.CustomException;

import static site.concertseat.global.statuscode.ErrorCode.BAD_REQUEST;
import static site.concertseat.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public void approveReview(Member member, Long reviewId, ApproveReviewReq request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        validateReviewStatus(request.getReviewStatus());

        ReviewStatus status = ReviewStatus.valueOf(request.getReviewStatus().toUpperCase());

        if(status == ReviewStatus.APPROVED) {
            throw new CustomException(BAD_REQUEST);
        }

        updateReviewStatus(review, status);

        if(status == ReviewStatus.REJECTED) {
            validateRejectReason(request.getRejectReason());
            updateRejectReason(review, request.getRejectReason());
        }
    }

    private void updateReviewStatus(Review review, ReviewStatus status) {
        review.updateStatus(status);
    }

    private void updateRejectReason(Review review, String rejectReason) {
        review.updateRejectReason(rejectReason);
    }

    private void validateReviewStatus(String reviewStatus) {
        try {
            EnumUtils.findEnumInsensitiveCase(ReviewStatus.class, reviewStatus);
        } catch (IllegalArgumentException e) {
            throw new CustomException(BAD_REQUEST);
        }
    }

    private void validateRejectReason(String rejectReason) {
        if(rejectReason == null || rejectReason.trim().isEmpty()) {
            throw new CustomException(BAD_REQUEST);
        }
    }
}
