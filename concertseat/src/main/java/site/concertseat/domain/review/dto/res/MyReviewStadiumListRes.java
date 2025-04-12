package site.concertseat.domain.review.dto.res;

import lombok.Data;
import site.concertseat.domain.review.dto.MyReviewStadiumDto;

import java.util.List;

@Data
public class MyReviewStadiumListRes {
    private List<MyReviewStadiumDto> stadiums;

    public MyReviewStadiumListRes(List<MyReviewStadiumDto> stadiums) {
        this.stadiums = stadiums;
    }
}
