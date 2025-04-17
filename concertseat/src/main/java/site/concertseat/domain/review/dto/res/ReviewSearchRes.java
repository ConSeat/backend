package site.concertseat.domain.review.dto.res;

import lombok.Data;
import site.concertseat.domain.review.dto.ReviewDto;
import site.concertseat.domain.review.dto.ReviewStatsDto;
import site.concertseat.domain.review.dto.ReviewWithLikesCount;
import java.util.ArrayList;
import java.util.List;

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
}
