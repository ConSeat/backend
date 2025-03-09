package site.concertseat.domain.review.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.concertseat.domain.concert.entity.Concert;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.enums.Distance;
import site.concertseat.domain.stadium.entity.Seating;
import site.concertseat.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = " UPDATE Review SET is_deleted = true WHERE review = ? ")
@SQLRestriction("is_deleted = false")
@EntityListeners(AuditingEntityListener.class)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seating_id", nullable = false)
    private Seating seating;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @NotNull
    @Column(length = 300)
    private String contents;

    @NotNull
    @Column
    private String thumbnail;

    @NotNull
    @Enumerated
    private Distance stageDistance;

    @NotNull
    @Enumerated
    private Distance thrustStageDistance;

    @NotNull
    @Enumerated
    private Distance screenDistance;

    @NotNull
    @Column
    private Boolean isApproved;
}
