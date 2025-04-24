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
import site.concertseat.domain.admin.dto.req.ChangeReviewStatusReq;
import site.concertseat.domain.admin.dto.res.AdminReviewDetails;
import site.concertseat.domain.admin.dto.res.AdminReviewListRes;
import site.concertseat.domain.admin.repository.AdminReviewRepository;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.entity.Feature;
import site.concertseat.domain.review.entity.Obstruction;
import site.concertseat.domain.review.entity.Review;
import site.concertseat.domain.review.entity.Sight;
import site.concertseat.domain.review.enums.ReviewStatus;
import site.concertseat.global.dto.PageDto;
import site.concertseat.global.exception.CustomException;
import site.concertseat.global.s3.S3Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static site.concertseat.domain.review.enums.ReviewStatus.APPROVED;
import static site.concertseat.domain.review.enums.ReviewStatus.REJECTED;
import static site.concertseat.global.statuscode.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminReviewRepository reviewRepository;
    private final S3Service s3Service;

    @Override
    public AdminReviewListRes findAdminReviews(Pageable pageable, AdminReviewListReq adminReviewListReq) {
        Page<AdminReviewDto> adminReviews = reviewRepository.findAdminReviews(pageable, adminReviewListReq);

        setBookmarksAndLikesCount(adminReviews);

        return new AdminReviewListRes(new PageDto<>(adminReviews));
    }

    @Override
    public AdminReviewDetails findAdminReview(Long reviewId) {
        AdminReviewDetails review = reviewRepository.findAdminReview(reviewId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        setImages(review);
        setFeatures(review);
        setObstructions(review);

        return review;
    }

    private void setImages(AdminReviewDetails review) {
        List<Sight> sights = reviewRepository.findSightsByReview(review.getReviewId());

        review.setImages(sights.stream()
                .map(Sight::getCompressedImage)
                .toList());
    }

    private void setFeatures(AdminReviewDetails review) {
        List<Feature> features = reviewRepository.findFeaturesByReview(review.getReviewId());

        review.setFeatures(features.stream()
                .map(Feature::getName)
                .toList());
    }

    private void setObstructions(AdminReviewDetails review) {
        List<Obstruction> obstructions = reviewRepository.findObstructionsByReview(review.getReviewId());

        review.setObstructions(obstructions.stream()
                .map(Obstruction::getName)
                .toList());
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
    public void changeReviewStatus(Member member, Long reviewId, ChangeReviewStatusReq request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        validateReviewStatus(request.getReviewStatus());

        ReviewStatus status = ReviewStatus.valueOf(request.getReviewStatus().toUpperCase());

        updateReviewStatus(review, status);

        if (status == APPROVED) {
            updateCompressedImage(review);
        }

        if (status == REJECTED) {
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

    private void updateCompressedImage(Review review) {
        List<Sight> sights = reviewRepository.findSightsByReview(review.getId());

        for (Sight sight : sights) {
            if (!sight.getImage().equals(sight.getCompressedImage())) continue;

            try {
                String compressedImage = s3Service.uploadCompressedImage(sight.getImage());

                sight.updateCompressedImage(compressedImage);
            } catch (Exception ignored) {}
        }
    }
}
