package site.concertseat.domain.review.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReviewImagesRes {
    private List<String> images;
}
