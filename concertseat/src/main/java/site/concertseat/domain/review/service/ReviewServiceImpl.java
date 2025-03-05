package site.concertseat.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.util.EnumUtils;
import site.concertseat.domain.concert.entity.Concert;
import site.concertseat.domain.concert.repository.ConcertRepository;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.dto.ReviewPostReq;
import site.concertseat.domain.review.entity.*;
import site.concertseat.domain.review.enums.Distance;
import site.concertseat.domain.review.repository.*;
import site.concertseat.domain.stadium.entity.Seating;
import site.concertseat.domain.stadium.repository.SeatingRepository;
import site.concertseat.global.exception.CustomException;

import java.util.List;

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
}
