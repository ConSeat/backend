package site.concertseat.domain.admin.dto.req;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Data
public class AdminReviewListReq {
    @DateTimeFormat(iso = DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DATE)
    private LocalDate endDate;

    private Integer stadiumId;

    private Integer sectionId;

    private String status;

    private String query;
}
