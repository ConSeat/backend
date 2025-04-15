package site.concertseat.domain.review.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.review.dto.MyReviewDetailDto;
import site.concertseat.domain.review.dto.MyReviewStadiumDto;
import site.concertseat.domain.review.dto.ReviewStatsDto;
import site.concertseat.domain.review.dto.ReviewWithLikesCount;
import site.concertseat.domain.review.entity.Review;
import site.concertseat.domain.review.enums.ReviewStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    @Query("select new site.concertseat.domain.review.dto.ReviewWithLikesCount(r, count(l)) " +
            "from Review r " +
            "join fetch r.member m " +
            "join fetch r.concert c " +
            "left join Likes l on l.review = r " +
            "where r.seating.id = :seatingId " +
            "and r.status = :status " +
            "group by r " +
            "order by count(l) desc, r.createdAt desc")
    List<ReviewWithLikesCount> findReviewsBySeatingId(@Param("seatingId") Integer seatingId,
                                                      @Param("status") ReviewStatus status,
                                                      Pageable pageable);

    @Query("select new site.concertseat.domain.review.dto.ReviewStatsDto( " +
            "   avg(r.stageDistance), " +
            "   avg(r.thrustStageDistance), " +
            "   avg(r.screenDistance), " +
            "   count(r)) " +
            "from Review r " +
            "where r.seating.id = :seatingId " +
            "and r.status = :status")
    ReviewStatsDto findReviewStats(@Param("seatingId") Integer seatingId,
                                   @Param("status") ReviewStatus status);

    @Query("select count(r) " +
            "from Review r " +
            "where r.status = :status")
    Integer countApprovedReviews(@Param("status") ReviewStatus status);

    @Query("select count(r) " +
            "from Review r " +
            "where r.member.id = :memberId")
    Long countMemberReviews(@Param("memberId") Long memberId);

    @Query("select distinct new site.concertseat.domain.review.dto.MyReviewStadiumDto(" +
            "r.concert.stadium.id," +
            "r.concert.stadium.name) " +
            "from Review r " +
            "where r.member.id = :memberId")
    List<MyReviewStadiumDto> findStadiumByMemberId(@Param("memberId") Long memberId);

    @Query("select r " +
            "from Review r " +
            "where r.status = 'APPROVED' " +
            "and r.id = :reviewId")
    Optional<Review> findApprovedReview(@Param("reviewId") Long reviewId);

    @Query("select new site.concertseat.domain.review.dto.MyReviewDetailDto(" +
            "r.id," +
            "r.member.nickname," +
            "r.member.src," +
            "r.concert.name," +
            "r.contents," +
            "r.createdAt," +
            "r.status," +
            "r.rejectionReason)" +
            "from Review r " +
            "where r.member.id = :memberId " +
            "and r.id = :reviewId")
    Optional<MyReviewDetailDto> findMyReview(@Param("memberId") Long memberId, @Param("reviewId") Long reviewId);
}
