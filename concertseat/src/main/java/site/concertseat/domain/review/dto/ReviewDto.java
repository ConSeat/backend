package site.concertseat.domain.review.dto;

import lombok.Builder;
import lombok.Data;
import site.concertseat.domain.review.entity.Review;
import site.concertseat.global.util.DateFormatter;

import java.util.List;

@Data
@Builder
public class ReviewDto {
    private Long reviewId;

    private String writerNickname;

    private String writerSrc;

    private String concertName;

    private List<String> images;

    private String contents;

    private String createdAt;

    private List<String> features;

    private List<String> obstructions;

    private Long likesCount;

    private Boolean isLiked;

    private Boolean isBookmarked;

    public static ReviewDto toDto(ReviewWithLikesCount reviewWithLikesCount) {
        Review review = reviewWithLikesCount.getReview();
        Long likesCount = reviewWithLikesCount.getLikesCount();

        return ReviewDto.builder()
                .reviewId(review.getId())
                .writerNickname(review.getMember().getNickname())
                .writerSrc(review.getMember().getSrc())
                .concertName(review.getConcert().getName())
                .contents(review.getContents())
                .createdAt(DateFormatter.calculateTime(review.getCreatedAt()))
                .likesCount(likesCount)
                .isLiked(false)
                .isBookmarked(false)
                .build();
    }
}
