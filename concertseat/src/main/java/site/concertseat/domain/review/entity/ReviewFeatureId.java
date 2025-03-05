package site.concertseat.domain.review.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewFeatureId implements Serializable {
    private Long reviewId;

    private Integer featureId;
}
