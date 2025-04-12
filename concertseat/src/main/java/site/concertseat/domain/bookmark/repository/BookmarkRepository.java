package site.concertseat.domain.bookmark.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.bookmark.dto.BookmarkStadiumDto;
import site.concertseat.domain.bookmark.entity.Bookmark;
import site.concertseat.domain.bookmark.entity.BookmarkId;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId>, BookmarkRepositoryCustom {
    @Query("select b " +
            "from Bookmark b " +
            "where b.review.id in :reviews " +
            "and b.member.id = :memberId")
    List<Bookmark> findBookmarkByReviewsAndMemberId(@Param("reviews") List<Long> reviews, @Param("memberId") Long memberId);

    @Query("select count(b) " +
            "from Bookmark b " +
            "where b.member.id = :memberId")
    Long countBookmarkByMemberId(@Param("memberId") Long memberId);

    @Query("select distinct new site.concertseat.domain.bookmark.dto.BookmarkStadiumDto(" +
            "b.review.concert.stadium.id," +
            "b.review.concert.stadium.name) " +
            "from Bookmark b " +
            "where b.member.id = :memberId")
    List<BookmarkStadiumDto> findStadiums(@Param("memberId") Long memberId);
}
