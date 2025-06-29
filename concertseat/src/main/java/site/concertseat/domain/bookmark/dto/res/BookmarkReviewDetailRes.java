package site.concertseat.domain.bookmark.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.concertseat.domain.bookmark.dto.ReviewDetailDto;
import site.concertseat.global.util.DateFormatter;

import java.util.List;

@Data
@AllArgsConstructor
public class BookmarkReviewDetailRes {
    private Long reviewId;

    private String writerNickname;

    private String writerSrc;

    private Integer stadiumId;

    private String stadiumName;

    private Integer sectionId;

    private String sectionName;

    private Integer seatingId;

    private String seatingName;

    private String concertName;

    private List<String> images;

    private String contents;

    private String createdAt;

    private List<String> features;

    private List<String> obstructions;

    private Boolean isBookmarked;

    public BookmarkReviewDetailRes(ReviewDetailDto reviewDetailDto, List<String> images, List<String> features, List<String> obstructions) {
        this.reviewId = reviewDetailDto.getReviewId();
        this.writerNickname = reviewDetailDto.getWriterNickname();
        this.writerSrc = reviewDetailDto.getWriterSrc();
        this.stadiumId = reviewDetailDto.getStadiumId();
        this.stadiumName = reviewDetailDto.getStadiumName();
        this.sectionId = reviewDetailDto.getSectionId();
        this.sectionName = reviewDetailDto.getSectionName();
        this.seatingId = reviewDetailDto.getSeating().getId();
        this.seatingName = reviewDetailDto.getSeating().getName();
        this.concertName = reviewDetailDto.getConcertName();
        this.images = images;
        this.contents = reviewDetailDto.getContents();
        this.createdAt = DateFormatter.calculateTime(reviewDetailDto.getCreatedAt());
        this.features = features;
        this.obstructions = obstructions;
        this.isBookmarked = true;
    }
}
