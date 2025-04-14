package site.concertseat.domain.bookmark.service;

import org.springframework.data.domain.Pageable;
import site.concertseat.domain.bookmark.dto.req.BookmarkReviewSearchReq;
import site.concertseat.domain.bookmark.dto.res.BookmarkReviewDetailRes;
import site.concertseat.domain.bookmark.dto.res.BookmarkReviewSearchRes;
import site.concertseat.domain.bookmark.dto.res.BookmarkStadiumListRes;
import site.concertseat.domain.member.entity.Member;

public interface BookmarkService {
    BookmarkStadiumListRes findStadium(Member member);

    BookmarkReviewSearchRes searchReview(Member member, BookmarkReviewSearchReq req, Pageable pageable);

    void addBookmark(Member member, Long reviewId);

    void deleteBookmark(Member member, Long reviewId);

    BookmarkReviewDetailRes reviewDetails(Member member, Long reviewId);
}
