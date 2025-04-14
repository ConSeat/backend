package site.concertseat.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewDetailDto {
    private Long reviewId;

    private String writerNickname;

    private String writerSrc;

    private String concertName;

    private String contents;

    private LocalDateTime createdAt;
}
