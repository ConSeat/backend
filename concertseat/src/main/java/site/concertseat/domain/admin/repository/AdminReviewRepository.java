package site.concertseat.domain.admin.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.admin.dto.BookmarksAndLikesCountDto;
import site.concertseat.domain.review.entity.Review;

import java.util.List;

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
}
