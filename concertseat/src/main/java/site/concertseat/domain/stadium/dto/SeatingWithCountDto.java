package site.concertseat.domain.stadium.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatingWithCountDto {
    private Integer seatingId;

    private String name;

    private Long reviewCount;
}
