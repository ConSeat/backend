package site.concertseat.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.concertseat.domain.review.enums.ReviewStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MyReviewDetailDto {
    private Long reviewId;

    private Long writerId;

    private String writerNickname;

    private String writerSrc;

    private Integer stadiumId;

    private String stadiumName;

    private Integer sectionId;

    private String sectionName;

    private Integer seatingId;

    private String seatingName;

    private String concertName;

    private String contents;

    private LocalDateTime createdAt;

    private ReviewStatus status;

    private String rejectReason;
}
