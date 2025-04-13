package site.concertseat.domain.review.dto.res;

import lombok.Data;
import site.concertseat.domain.review.dto.MyReviewDto;
import site.concertseat.global.dto.SliceDto;

@Data
public class MyReviewSearchRes {
    private SliceDto<MyReviewDto> reviews;

    public MyReviewSearchRes(SliceDto<MyReviewDto> reviews) {
        this.reviews = reviews;
    }
}