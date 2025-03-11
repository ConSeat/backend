package site.concertseat.domain.review.dto;

import lombok.Data;
import site.concertseat.domain.review.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ReviewSearchRes {
    private String distanceMessage;

    private List<String> thumbnails = new ArrayList<>();

    private Long reviewCount;

    private List<ReviewDto> reviews;

    public ReviewSearchRes(ReviewStatsDto reviewStats, List<ReviewWithLikesCount> reviews) {
        this.distanceMessage = reviewStats.getMessage();
        this.reviewCount = reviewStats.getReviewCount();
        this.reviews = reviews.stream().map(ReviewDto::toDto).toList();
    }

    public void setImages(Map<Long, List<Sight>> sightMap) {
        for (ReviewDto review : reviews) {
            List<Sight> sights = sightMap.get(review.getReviewId());

            this.thumbnails.add(sights.getFirst().getImage());
            review.setImages(sights.stream().map(Sight::getCompressedImage).toList());
        }
    }

    public void setFeatures(Map<Long, List<ReviewFeature>> feeatureMap) {
        for (ReviewDto review : reviews) {
            List<String> features = feeatureMap.get(review.getReviewId())
                    .stream()
                    .map(ReviewFeature::getFeature)
                    .map(Feature::getName)
                    .toList();

            review.setFeatures(features);
        }
    }

    public void setObstructions(Map<Long, List<ReviewObstruction>> obstructionMap) {
        for (ReviewDto review : reviews) {
            List<String> obstructions = obstructionMap.get(review.getReviewId())
                    .stream()
                    .map(ReviewObstruction::getObstruction)
                    .map(Obstruction::getName)
                    .toList();

            review.setObstructions(obstructions);
        }
    }
}
