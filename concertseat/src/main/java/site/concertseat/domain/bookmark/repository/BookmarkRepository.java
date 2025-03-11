package site.concertseat.domain.bookmark.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.bookmark.entity.Bookmark;
import site.concertseat.domain.bookmark.entity.BookmarkId;
import site.concertseat.domain.review.entity.Review;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
    @Query("select b " +
            "from Bookmark b " +
            "where b.review in :reviews " +
            "and b.member.id = :memberId")
    List<Bookmark> findBookmarkByReviewsAndMemberId(@Param("reviews") List<Review> reviews, @Param("memberId") Long memberId);
}
