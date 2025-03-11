package site.concertseat.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.util.EnumUtils;
import site.concertseat.domain.bookmark.entity.Bookmark;
import site.concertseat.domain.bookmark.repository.BookmarkRepository;
import site.concertseat.domain.concert.entity.Concert;
import site.concertseat.domain.concert.repository.ConcertRepository;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.dto.*;
import site.concertseat.domain.review.entity.*;
import site.concertseat.domain.review.enums.Distance;
import site.concertseat.domain.review.repository.*;
import site.concertseat.domain.stadium.entity.Seating;
import site.concertseat.domain.stadium.repository.SeatingRepository;
import site.concertseat.global.exception.CustomException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static site.concertseat.global.statuscode.ErrorCode.BAD_REQUEST;
import static site.concertseat.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final SeatingRepository seatingRepository;
    private final ConcertRepository concertRepository;
    private final ObstructionRepository obstructionRepository;
    private final FeatureRepository featureRepository;
    private final BatchRepository batchRepository;
    private final SightRepository sightRepository;
    private final LikesRepository likesRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CacheService cacheService;

    @Override
    @Transactional
    public void postReview(Member member, Integer seatingId, Integer concertId, ReviewPostReq reviewPostReq) {
        validateDistance(reviewPostReq);

        Seating seating = seatingRepository.findById(seatingId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Review review = reviewPostReq.toEntity(member, seating, concert);
        reviewRepository.save(review);

        saveSights(reviewPostReq.getImages(), review);
        saveObstructions(reviewPostReq.getObstructions(), review);
        saveFeatures(reviewPostReq.getFeatures(), review);
    }

    private void validateDistance(ReviewPostReq reviewPostReq) {
        try {
            EnumUtils.findEnumInsensitiveCase(Distance.class, reviewPostReq.getStageDistance());
            EnumUtils.findEnumInsensitiveCase(Distance.class, reviewPostReq.getThrustStageDistance());
            EnumUtils.findEnumInsensitiveCase(Distance.class, reviewPostReq.getScreenDistance());
        } catch (IllegalArgumentException e) {
            throw new CustomException(BAD_REQUEST);
        }
    }

    private void saveSights(List<String> images, Review review) {
        List<Sight> sights = images.stream()
                .distinct()
                .map(image ->
                        Sight.builder()
                                .review(review)
                                .image(image)
                                .compressedImage(image) // todo: 압축된 이미지는 처리
                                .build())
                .toList();

        batchRepository.saveSights(sights);
    }

    private void saveObstructions(List<Integer> obstructions, Review review) {
        List<Obstruction> existingObstructions = obstructionRepository.findExistingObstructions(obstructions);

        long queryCount = obstructions.stream().distinct().count();
        long dbCount = existingObstructions.size();

        if (queryCount != dbCount) {
            throw new CustomException(BAD_REQUEST);
        }

        List<ReviewObstruction> reviewObstructions = existingObstructions.stream()
                .map(obstruction ->
                        ReviewObstruction.builder()
                                .id(new ReviewObstructionId(review.getId(), obstruction.getId()))
                                .review(review)
                                .obstruction(obstruction)
                                .build())
                .toList();

        batchRepository.saveObstructions(reviewObstructions);
    }

    private void saveFeatures(List<Integer> features, Review review) {
        List<Feature> existingFeatures = featureRepository.findExistingFeatures(features);

        long queryCount = features.stream().distinct().count();
        long dbCount = existingFeatures.size();

        if (queryCount != dbCount) {
            throw new CustomException(BAD_REQUEST);
        }

        List<ReviewFeature> reviewObstructions = existingFeatures.stream()
                .map(feature ->
                        ReviewFeature.builder()
                                .id(new ReviewFeatureId(review.getId(), feature.getId()))
                                .review(review)
                                .feature(feature)
                                .build())
                .toList();

        batchRepository.saveFeatures(reviewObstructions);
    }

    @Override
    public ReviewSearchRes searchReview(Member member, Integer seatingId) {
        if (!seatingRepository.existsById(seatingId)) {
            throw new CustomException(NOT_FOUND);
        }

        int reviewCount = cacheService.getReviewCount(seatingId);

        List<ReviewWithLikesCount> reviewsWithLikesCount = reviewRepository
                .findReviewsBySeatingId(seatingId, Pageable.ofSize(3));

        List<Review> reviews = reviewsWithLikesCount.stream()
                .map(ReviewWithLikesCount::getReview).toList();

        if (reviews.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }

        ReviewStatsDto averageDistance = reviewRepository.findAverageDistance(seatingId);

        ReviewSearchRes result = new ReviewSearchRes(averageDistance, reviewCount, reviewsWithLikesCount);

        setImages(reviews, result);
        setFeatures(reviews, result);
        setObstructions(reviews, result);
        setIsLikedAndIsBookmarked(member, reviews, result);

        return result;
    }

    private void setImages(List<Review> reviews, ReviewSearchRes res) {
        Map<Long, List<Sight>> sights = sightRepository.findByReviews(reviews)
                .stream()
                .collect(Collectors.groupingBy(sight -> sight.getReview().getId()));

        res.setImages(sights);
    }

    private void setFeatures(List<Review> reviews, ReviewSearchRes res) {
        Map<Long, List<ReviewFeature>> features = featureRepository.findReviewFeatures(reviews)
                .stream()
                .collect(Collectors.groupingBy(reviewFeature -> reviewFeature.getReview().getId()));

        res.setFeatures(features);
    }

    private void setObstructions(List<Review> reviews, ReviewSearchRes res) {
        Map<Long, List<ReviewObstruction>> obstructions = obstructionRepository.findReviewObstruction(reviews)
                .stream()
                .collect(Collectors.groupingBy(reviewObstruction -> reviewObstruction.getReview().getId()));

        res.setObstructions(obstructions);
    }

    private void setIsLikedAndIsBookmarked(Member member, List<Review> reviews, ReviewSearchRes res) {
        if (member == null) {
            return;
        }

        Map<Long, List<Likes>> likes = likesRepository.findLikesByReviewsAndMemberId(reviews, member.getId())
                .stream().collect(Collectors.groupingBy(like -> like.getReview().getId()));

        Map<Long, List<Bookmark>> bookmarks = bookmarkRepository.findBookmarkByReviewsAndMemberId(reviews, member.getId())
                .stream().collect(Collectors.groupingBy(bookmark -> bookmark.getReview().getId()));

        for (ReviewDto review : res.getReviews()) {
            review.setIsLiked(likes.get(review.getReviewId()) != null);
            review.setIsBookmarked(bookmarks.get(review.getReviewId()) != null);
        }
    }
}
