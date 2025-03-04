package site.concertseat.domain.review.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewFeatureId {
    private Long reviewId;

    private Integer featureId;
}
