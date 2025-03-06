package site.concertseat.domain.bookmark.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkId {
    private Long memberId;

    private Integer seatingId;
}
