package site.concertseat.domain.review.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.review.entity.Likes;
import site.concertseat.domain.review.entity.LikesId;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, LikesId> {
    @Query("select l " +
            "from Likes l " +
            "where l.review.id in :reviews " +
            "and l.member.id = :memberId")
    List<Likes> findLikesByReviewsAndMemberId(@Param("reviews") List<Long> reviews, @Param("memberId") Long memberId);
}
