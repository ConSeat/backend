package site.concertseat.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyReviewStadiumDto {
    private Integer stadiumId;

    private String stadiumName;
}
