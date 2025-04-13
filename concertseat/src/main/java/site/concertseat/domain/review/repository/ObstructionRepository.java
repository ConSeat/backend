package site.concertseat.domain.review.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.review.entity.Obstruction;
import site.concertseat.domain.review.entity.ReviewObstruction;
import site.concertseat.domain.review.entity.ReviewObstructionId;

import java.util.List;

@Repository
public interface ObstructionRepository extends JpaRepository<ReviewObstruction, ReviewObstructionId> {
    @Query("select o " +
            "from Obstruction o " +
            "where o.id in :obstructions")
    List<Obstruction> findExistingObstructions(@Param("obstructions") List<Integer> obstructions);

    @EntityGraph(attributePaths = "obstruction")
    @Query("select ro " +
            "from ReviewObstruction ro " +
            "where ro.review.id in :reviews")
    List<ReviewObstruction> findReviewObstruction(@Param("reviews") List<Long> reviews);

    @Query("select o " +
            "from Obstruction o")
    List<Obstruction> findAllObstructions();

    @Query("select ro.obstruction.name " +
            "from ReviewObstruction ro " +
            "where ro.review.id = :reviewId")
    List<String> findObstructionByReviewId(@Param("reviewId") Long reviewId);
}
