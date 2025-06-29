package site.concertseat.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.concertseat.domain.stadium.entity.Seating;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewDetailDto {
    private Long reviewId;

    private String writerNickname;

    private String writerSrc;

    private LocalDateTime createdAt;

    private String contents;

    private Integer stadiumId;

    private String stadiumName;

    private Integer sectionId;

    private String sectionName;

    private Integer seatingId;

    private String seatingName;

    private String concertName;

    private Seating seating;
}
