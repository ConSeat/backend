package site.concertseat.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.concertseat.global.dto.SliceDto;

@Data
@AllArgsConstructor
public class ReviewListRes {
    private SliceDto<ReviewDto> reviews;
}
