package site.concertseat.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.concertseat.domain.review.enums.ReviewStatus;

@Data
@AllArgsConstructor
public class MyReviewDto {
    private Long reviewId;

    private String thumbnailUrl;

    private String floorName;

    private String sectionName;

    private String seatingName;

    private ReviewStatus status;
}
