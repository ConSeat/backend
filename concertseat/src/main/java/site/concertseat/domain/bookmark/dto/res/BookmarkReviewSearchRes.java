package site.concertseat.domain.bookmark.dto.res;

import lombok.Data;
import site.concertseat.domain.bookmark.dto.BookmarkReviewDto;
import site.concertseat.global.dto.SliceDto;

@Data
public class BookmarkReviewSearchRes {
    private SliceDto<BookmarkReviewDto> reviews;

    public BookmarkReviewSearchRes(SliceDto<BookmarkReviewDto> reviews) {
        this.reviews = reviews;
    }
}
