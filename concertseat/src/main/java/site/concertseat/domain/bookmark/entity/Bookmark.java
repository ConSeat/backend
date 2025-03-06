package site.concertseat.domain.bookmark.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.stadium.entity.Seating;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark {
    @EmbeddedId
    private BookmarkId bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("memberId")
    @JoinColumn(name = "member_id", columnDefinition = "INT UNSIGNED", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("seatingId")
    @JoinColumn(name = "seating_id", columnDefinition = "SMALLINT", nullable = false)
    private Seating seating;
}
