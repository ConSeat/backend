package site.concertseat.domain.admin.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.concertseat.domain.admin.dto.AdminReviewDto;

import java.util.List;

@Data
@AllArgsConstructor
public class AllReviewListRes {
    private List<AdminReviewDto> adminReviews;
}
