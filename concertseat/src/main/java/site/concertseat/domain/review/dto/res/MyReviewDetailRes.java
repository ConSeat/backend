package site.concertseat.domain.review.dto.res;

import lombok.Data;
import site.concertseat.domain.review.dto.MyReviewDetailDto;
import site.concertseat.domain.review.enums.ReviewStatus;
import site.concertseat.global.util.DateFormatter;

import java.util.List;

@Data
public class MyReviewDetailRes {
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

    private ReviewStatus status;

    private String rejectReason;

    public MyReviewDetailRes(MyReviewDetailDto reviewDetailDto, List<String> images, List<String> features, List<String> obstructions) {
        this.reviewId = reviewDetailDto.getReviewId();
        this.writerNickname = reviewDetailDto.getWriterNickname();
        this.writerSrc = reviewDetailDto.getWriterSrc();
        this.stadiumId = reviewDetailDto.getStadiumId();
        this.stadiumName = reviewDetailDto.getStadiumName();
        this.sectionId = reviewDetailDto.getSectionId();
        this.sectionName = reviewDetailDto.getSectionName();
        this.seatingId = reviewDetailDto.getSeatingId();
        this.seatingName = reviewDetailDto.getSeatingName();
        this.concertName = reviewDetailDto.getConcertName();
        this.images = images;
        this.contents = reviewDetailDto.getContents();
        this.createdAt = DateFormatter.calculateTime(reviewDetailDto.getCreatedAt());
        this.features = features;
        this.obstructions = obstructions;
        this.status = reviewDetailDto.getStatus();
        this.rejectReason = reviewDetailDto.getRejectReason();
    }
}
