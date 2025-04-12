package site.concertseat.domain.review.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.concertseat.domain.review.dto.ReviewDto;
import site.concertseat.global.dto.SliceDto;

@Data
@AllArgsConstructor
public class ReviewListRes {
    private SliceDto<ReviewDto> reviews;
}
