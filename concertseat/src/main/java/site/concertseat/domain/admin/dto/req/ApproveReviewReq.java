package site.concertseat.domain.admin.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApproveReviewReq {
    @NotBlank
    private String reviewStatus;

    private String rejectReason;

    @NotBlank
    private String inspectorName;
}
