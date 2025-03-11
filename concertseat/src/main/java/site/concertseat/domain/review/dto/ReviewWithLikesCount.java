package site.concertseat.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.concertseat.domain.review.entity.Review;

@Data
@AllArgsConstructor
public class ReviewWithLikesCount {
    private Review review;

    private Long likesCount;
}
