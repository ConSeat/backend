package site.concertseat.domain.admin.dto.res;

import lombok.Data;
import site.concertseat.domain.review.entity.Review;
import site.concertseat.domain.review.enums.ReviewStatus;
import site.concertseat.global.util.DateFormatter;

import java.util.List;

@Data
public class AdminReviewDetails {
    private Long reviewId;

    private String writerName;

    private String createdAt;

    private String stadiumName;

    private String concertName;

    private String floorName;

    private String sectionName;

    private String seatingName;

    private String contents;

    private ReviewStatus status;

    private List<String> images;

    private List<String> features;

    private List<String> obstructions;

    public AdminReviewDetails(Review review) {
        this.reviewId = review.getId();
        this.writerName = review.getMember().getNickname();
        this.createdAt = DateFormatter.convertToTime(review.getCreatedAt());
        this.stadiumName = review.getSeating().getSection().getFloor().getStadium().getName();
        this.concertName = review.getConcert().getName();
        this.floorName = review.getSeating().getSection().getFloor().getName();
        this.sectionName = review.getSeating().getSection().getName();
        this.seatingName = this.floorName.equals("FLOOR") ? "" : review.getSeating().getName();
        this.contents = review.getContents();
        this.status = review.getStatus();
    }
}
