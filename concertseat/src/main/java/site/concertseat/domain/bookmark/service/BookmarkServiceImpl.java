package site.concertseat.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.concertseat.domain.bookmark.dto.BookmarkReviewDto;
import site.concertseat.domain.bookmark.dto.BookmarkStadiumDto;
import site.concertseat.domain.bookmark.dto.ReviewDetailDto;
import site.concertseat.domain.bookmark.dto.req.BookmarkReviewSearchReq;
import site.concertseat.domain.bookmark.dto.res.BookmarkReviewDetailRes;
import site.concertseat.domain.bookmark.dto.res.BookmarkReviewSearchRes;
import site.concertseat.domain.bookmark.dto.res.BookmarkStadiumListRes;
import site.concertseat.domain.bookmark.repository.BookmarkRepository;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.repository.FeatureRepository;
import site.concertseat.domain.review.repository.ObstructionRepository;
import site.concertseat.domain.review.repository.SightRepository;
import site.concertseat.global.dto.SliceDto;
import site.concertseat.global.exception.CustomException;

import java.util.List;

import static site.concertseat.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final SightRepository sightRepository;
    private final FeatureRepository featureRepository;
    private final ObstructionRepository obstructionRepository;

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

    @Override
    public BookmarkReviewDetailRes reviewDetails(Member member, Long reviewId) {
        ReviewDetailDto reviewDto = bookmarkRepository.findBookmarkReview(member.getId(), reviewId);

        if(reviewDto == null) {
            throw new CustomException(NOT_FOUND);
        }

        List<String> images = sightRepository.findByReviewId(reviewId);
        List<String> features = featureRepository.findFeatureByReviewId(reviewId);
        List<String> obstructions = obstructionRepository.findObstructionByReviewId(reviewId);

        return new BookmarkReviewDetailRes(reviewDto, images, features, obstructions);
    }
}
