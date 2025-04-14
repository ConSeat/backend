package site.concertseat.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.concertseat.domain.review.enums.ReviewStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MyReviewDetailDto {
    private Long reviewId;

    private String writerNickname;

    private String writerSrc;

    private String concertName;

    private String contents;

    private LocalDateTime createdAt;

    private ReviewStatus status;

    private String rejectReason;
}
