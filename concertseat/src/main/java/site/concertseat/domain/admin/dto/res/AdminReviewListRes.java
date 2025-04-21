package site.concertseat.domain.admin.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.concertseat.domain.admin.dto.AdminReviewDto;
import site.concertseat.global.dto.PageDto;

@Data
@AllArgsConstructor
public class AdminReviewListRes {
    private PageDto<AdminReviewDto> adminReviews;
}
