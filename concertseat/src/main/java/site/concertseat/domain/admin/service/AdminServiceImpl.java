package site.concertseat.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.util.EnumUtils;
import site.concertseat.domain.admin.dto.AdminReviewDto;
import site.concertseat.domain.admin.dto.BookmarksAndLikesCountDto;
import site.concertseat.domain.admin.dto.req.AdminReviewListReq;
import site.concertseat.domain.admin.dto.req.ApproveReviewReq;
import site.concertseat.domain.admin.dto.res.AdminReviewListRes;
import site.concertseat.domain.admin.repository.AdminReviewRepository;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.entity.Review;
import site.concertseat.domain.review.enums.ReviewStatus;
import site.concertseat.global.dto.PageDto;
import site.concertseat.global.exception.CustomException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static site.concertseat.global.statuscode.ErrorCode.BAD_REQUEST;
import static site.concertseat.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminReviewRepository reviewRepository;

    @Override
    public AdminReviewListRes findAdminReviews(Pageable pageable, AdminReviewListReq adminReviewListReq) {
        Page<AdminReviewDto> adminReviews = reviewRepository.findAdminReviews(pageable, adminReviewListReq);

        setBookmarksAndLikesCount(adminReviews);

        return new AdminReviewListRes(new PageDto<>(adminReviews));
    }

    private void setBookmarksAndLikesCount(Page<AdminReviewDto> adminReviews) {
        List<Long> reviewIds = adminReviews.getContent().stream()
                .map(AdminReviewDto::getReviewId)
                .toList();

        Map<Long, BookmarksAndLikesCountDto> count = reviewRepository.countBookmarksAndLikes(reviewIds).stream()
                .collect(Collectors.toMap(
                        BookmarksAndLikesCountDto::getReviewId,
                        Function.identity()
                ));

        adminReviews.getContent().forEach(
                review -> {
                    BookmarksAndLikesCountDto bookmarksAndLikesCountDto = count.get(review.getReviewId());
                    review.setBookmarksCount(bookmarksAndLikesCountDto.getBookmarksCount());
                    review.setLikesCount(bookmarksAndLikesCountDto.getLikesCount());
                });
    }

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
