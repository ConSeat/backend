package site.concertseat.domain.review.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.review.entity.Feature;
import site.concertseat.domain.review.entity.ReviewFeature;
import site.concertseat.domain.review.entity.ReviewFeatureId;

import java.util.List;

@Repository
public interface FeatureRepository extends JpaRepository<ReviewFeature, ReviewFeatureId> {
    @Query("select f " +
            "from Feature f " +
            "where f.id in :features")
    List<Feature> findExistingFeatures(@Param("features") List<Integer> features);
}
