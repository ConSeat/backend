package site.concertseat.domain.review.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.review.entity.Review;
import site.concertseat.domain.review.entity.Sight;

import java.util.List;

@Repository
public interface SightRepository extends JpaRepository<Sight, Long> {
    @Query("select s " +
            "from Sight s " +
            "where s.review in :reviews")
    List<Sight> findByReviews(@Param("reviews") List<Review> reviews);
}
