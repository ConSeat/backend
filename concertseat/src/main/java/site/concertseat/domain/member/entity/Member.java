package site.concertseat.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.concertseat.domain.member.enums.Role;
import site.concertseat.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = " UPDATE Member SET is_deleted = true WHERE member_id = ? ")
@SQLRestriction("is_deleted = false")
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @NotNull
    @Column(columnDefinition = "CHAR(36)", unique = true)
    private String uuid;

    @NotNull
    @Column(length = 320, unique = true)
    private String socialId;

    @NotNull
    @Column(length = 20)
    private String nickname;

    @NotNull
    @Column(length = 320)
    private String src;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    private String providerType;
}
