package site.concertseat.domain.review.repository;

import io.lettuce.core.dynamic.annotation.Param;
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
}
