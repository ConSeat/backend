package site.concertseat.domain.bookmark.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import site.concertseat.domain.bookmark.dto.BookmarkReviewDto;

import java.time.LocalDateTime;

public interface BookmarkRepositoryCustom {
    Slice<BookmarkReviewDto> findBookmarkReviews(Long memberId, Integer stadiumId, LocalDateTime lastModifiedAt, Pageable pageable);
}
