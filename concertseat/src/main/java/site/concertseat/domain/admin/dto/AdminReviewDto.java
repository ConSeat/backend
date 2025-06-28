package site.concertseat.domain.admin.dto;

import lombok.Data;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.entity.Review;
import site.concertseat.domain.review.enums.ReviewStatus;
import site.concertseat.global.util.DateFormatter;

@Data
public class AdminReviewDto {
    private Long reviewId;

    private ReviewStatus status;

    private String writerName;

    private String createdDate;

    private String stadiumName;

    private String seatingInfo;

    private Long bookmarksCount;

    private Long likesCount;

    private String email;

    private String nickname;

    public AdminReviewDto(Review review) {
        this.reviewId = review.getId();
        this.status = review.getStatus();
        this.writerName = review.getMember().getNickname();
        this.createdDate = DateFormatter.convertToDate(review.getCreatedAt());
        this.stadiumName = review.getSeating().getSection().getFloor().getStadium().getName();
        this.seatingInfo = getSeatingInfo(review);
        this.email = review.getMember().getSocialId();
        this.nickname = review.getMember().getNickname();
    }

    private String getSeatingInfo(Review review) {
        String seatingInfo = review.getSeating().getSection().getFloor().getName() + " " +
                review.getSeating().getSection().getName();

        if (!review.getSeating().getName().equals("FLOOR")) {
            seatingInfo += " " + review.getSeating().getName();
        }

        return seatingInfo;
    }
}
