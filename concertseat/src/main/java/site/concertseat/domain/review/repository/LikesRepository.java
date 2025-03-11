package site.concertseat.domain.review.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.review.entity.Likes;
import site.concertseat.domain.review.entity.LikesId;
import site.concertseat.domain.review.entity.Review;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, LikesId> {
    @Query("select l " +
            "from Likes l " +
            "where l.review in :reviews " +
            "and l.member.id = :memberId")
    List<Likes> findLikesByReviewsAndMemberId(@Param("reviews") List<Review> reviews, @Param("memberId") Long memberId);
}
