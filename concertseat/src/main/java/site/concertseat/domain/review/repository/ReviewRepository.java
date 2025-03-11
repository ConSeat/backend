package site.concertseat.domain.review.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.review.dto.ReviewStatsDto;
import site.concertseat.domain.review.dto.ReviewWithLikesCount;
import site.concertseat.domain.review.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select new site.concertseat.domain.review.dto.ReviewWithLikesCount(r, count(l)) " +
            "from Review r " +
            "join fetch r.member m " +
            "join fetch r.concert c " +
            "left join Likes l on l.review = r " +
            "where r.seating.id = :seatingId " +
            "and r.isApproved = true " +
            "group by r " +
            "order by count(l) desc, r.createdAt desc")
    List<ReviewWithLikesCount> findReviewsBySeatingId(@Param("seatingId") Integer seatingId, Pageable pageable);

    @Query("select new site.concertseat.domain.review.dto.ReviewStatsDto( " +
            "avg(r.stageDistance), " +
            "avg(r.thrustStageDistance), " +
            "avg(r.screenDistance), " +
            "count(r) ) " +
            "from Review r " +
            "where r.seating.id = :seatingId " +
            "and r.isApproved = true")
    ReviewStatsDto findAverageDistance(@Param("seatingId") Integer seatingId);

    @Query("select count(r) " +
            "from Review r " +
            "where r.seating.id = :seatingId " +
            "and r.isApproved = true")
    Integer countReviewsBySeatingId(@Param("seatingId") Integer seatingId);
}
