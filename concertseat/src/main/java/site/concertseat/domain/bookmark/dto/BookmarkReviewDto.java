package site.concertseat.domain.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookmarkReviewDto {
    private Long reviewId;

    private String thumbnailUrl;

    private String floorName;

    private String sectionName;

    private String seatingName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime modifiedAt;
}
