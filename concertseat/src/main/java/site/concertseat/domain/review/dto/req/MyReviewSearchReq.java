package site.concertseat.domain.review.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MyReviewSearchReq {
    @NotNull
    private Integer stadiumId;

    private Long lastReviewId;
}
