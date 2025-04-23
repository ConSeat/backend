package site.concertseat.domain.admin.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeReviewStatusReq {
    @NotBlank
    private String reviewStatus;

    private String rejectReason;

    @NotBlank
    private String inspectorName;
}
