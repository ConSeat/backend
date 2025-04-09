package site.concertseat.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.concertseat.domain.bookmark.dto.BookmarkReviewDto;
import site.concertseat.domain.bookmark.dto.BookmarkStadiumDto;
import site.concertseat.domain.bookmark.dto.req.BookmarkReviewSearchReq;
import site.concertseat.domain.bookmark.dto.res.BookmarkReviewSearchRes;
import site.concertseat.domain.bookmark.dto.res.BookmarkStadiumListRes;
import site.concertseat.domain.bookmark.repository.BookmarkRepository;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.global.dto.SliceDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    @Override
    public BookmarkStadiumListRes findStadium(Member member) {
        List<BookmarkStadiumDto> stadiumDtoList = bookmarkRepository.findStadiums(member.getId());

        return new BookmarkStadiumListRes(stadiumDtoList);
    }

    @Override
    public BookmarkReviewSearchRes searchReview(Member member, BookmarkReviewSearchReq req, Pageable pageable) {
        Slice<BookmarkReviewDto> reviewDtoList = bookmarkRepository.findBookmarkReviews(member.getId(),
                req.getStadiumId(), req.getLastModifiedAt(), pageable);

        SliceDto<BookmarkReviewDto> reviewList = new SliceDto<>(reviewDtoList);

        return new BookmarkReviewSearchRes(reviewList);
    }
}
