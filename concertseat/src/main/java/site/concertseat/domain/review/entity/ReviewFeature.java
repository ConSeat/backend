package site.concertseat.domain.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewFeature {
    @EmbeddedId
    private ReviewFeatureId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("reviewId")
    @JoinColumn(name = "review_id", columnDefinition = "INT UNSIGNED", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("featureId")
    @JoinColumn(name = "feature_id", columnDefinition = "TINYINT", nullable = false)
    private Feature feature;
}
