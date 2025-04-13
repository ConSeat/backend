package site.concertseat.domain.review.dto.req;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewListReq {
    private List<Integer> features = new ArrayList<>();

    private List<Integer> obstructions = new ArrayList<>();

    private Long lastReviewId;
}
