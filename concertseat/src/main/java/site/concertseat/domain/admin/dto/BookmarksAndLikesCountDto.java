package site.concertseat.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookmarksAndLikesCountDto {
    private Long reviewId;

    private Long bookmarksCount;

    private Long likesCount;
}
