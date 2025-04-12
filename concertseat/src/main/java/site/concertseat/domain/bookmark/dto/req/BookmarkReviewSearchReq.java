package site.concertseat.domain.bookmark.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class BookmarkReviewSearchReq {
    @NotNull
    private Integer stadiumId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime lastModifiedAt;
}
