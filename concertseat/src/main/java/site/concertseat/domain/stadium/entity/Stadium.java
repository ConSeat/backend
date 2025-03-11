package site.concertseat.domain.stadium.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stadium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stadium_id", columnDefinition = "TINYINT")
    private Integer id;

    @NotNull
    @Column(length = 30)
    private String name;

    @NotNull
    @Column
    private String image;

    @NotNull
    @Column
    private Boolean isActive;
}
