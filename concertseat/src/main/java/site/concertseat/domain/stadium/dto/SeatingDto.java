package site.concertseat.domain.stadium.dto;

import lombok.Data;
import site.concertseat.domain.stadium.entity.Seating;

@Data
public class SeatingDto {
    private Integer seatingId;

    private String name;

    public SeatingDto(Seating seating) {
        this.seatingId = seating.getId();
        this.name = seating.getName();
    }
}
