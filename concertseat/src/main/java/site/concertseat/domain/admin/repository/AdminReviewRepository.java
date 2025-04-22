package site.concertseat.domain.admin.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.admin.dto.BookmarksAndLikesCountDto;
import site.concertseat.domain.admin.dto.res.AdminReviewDetails;
import site.concertseat.domain.review.entity.Feature;
import site.concertseat.domain.review.entity.Obstruction;
import site.concertseat.domain.review.entity.Review;
import site.concertseat.domain.review.entity.Sight;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminReviewRepository extends JpaRepository<Review, Long>, CustomAdminReviewRepository {
    @Query("select new site.concertseat.domain.admin.dto.BookmarksAndLikesCountDto(" +
            "   r.id, count(distinct b), count(distinct l)) " +
            "from Review r " +
            "left join Likes l " +
            "on l.review = r " +
            "left join Bookmark b " +
            "on b.review = r " +
            "where r.id in :reviewIds " +
            "group by r.id")
    List<BookmarksAndLikesCountDto> countBookmarksAndLikes(@Param("reviewIds") List<Long> reviewIds);

    @Query("select new site.concertseat.domain.admin.dto.res.AdminReviewDetails(r) " +
            "from Review r " +
            "join fetch r.member " +
            "join fetch r.concert " +
            "join fetch r.seating s " +
            "join fetch s.section sec " +
            "join fetch sec.floor f " +
            "join fetch f.stadium " +
            "where r.id = :reviewId")
    Optional<AdminReviewDetails> findAdminReview(@Param("reviewId") Long reviewId);

    @Query("select s " +
            "from Sight s " +
            "where s.review.id = :reviewId")
    List<Sight> findSightsByReview(@Param("reviewId") Long reviewId);

    @Query("select f " +
            "from Feature f " +
            "join ReviewFeature rf " +
            "on rf.feature = f " +
            "where rf.review.id = :reviewId")
    List<Feature> findFeaturesByReview(@Param("reviewId") Long reviewId);

    @Query("select o " +
            "from Obstruction o " +
            "join ReviewObstruction ro " +
            "on ro.obstruction = o " +
            "where ro.review.id = :reviewId")
    List<Obstruction> findObstructionsByReview(@Param("reviewId") Long reviewId);
}
