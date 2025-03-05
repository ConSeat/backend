package site.concertseat.domain.review.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import site.concertseat.domain.review.entity.ReviewFeature;
import site.concertseat.domain.review.entity.ReviewObstruction;
import site.concertseat.domain.review.entity.Sight;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BatchRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveSights(List<Sight> sights){
        String sql = "insert into sight(compressed_image, image, review_id) values (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                sights,
                sights.size(),
                (PreparedStatement ps, Sight sight) -> {
                    ps.setString(1, sight.getCompressedImage());
                    ps.setString(2, sight.getImage());
                    ps.setString(3, String.valueOf(sight.getReview().getId()));
                });
    }

    @Transactional
    public void saveObstructions(List<ReviewObstruction> obstructions){
        String sql = "insert into review_obstruction(review_id, obstruction_id) values (?, ?)";

        jdbcTemplate.batchUpdate(sql,
                obstructions,
                obstructions.size(),
                (PreparedStatement ps, ReviewObstruction obstruction) -> {
                    ps.setString(1, String.valueOf(obstruction.getId().getReviewId()));
                    ps.setString(2, String.valueOf(obstruction.getId().getObstructionId()));
                });
    }

    @Transactional
    public void saveFeatures(List<ReviewFeature> features){
        String sql = "insert into review_feature(review_id, feature_id) values (?, ?)";

        jdbcTemplate.batchUpdate(sql,
                features,
                features.size(),
                (PreparedStatement ps, ReviewFeature feature) -> {
                    ps.setString(1, String.valueOf(feature.getId().getReviewId()));
                    ps.setString(2, String.valueOf(feature.getId().getFeatureId()));
                });
    }
}
