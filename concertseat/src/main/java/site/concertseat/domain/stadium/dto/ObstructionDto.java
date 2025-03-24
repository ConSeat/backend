package site.concertseat.domain.stadium.dto;

import lombok.Data;
import site.concertseat.domain.review.entity.Obstruction;

@Data
public class ObstructionDto {
    private Integer obstructionId;

    private String name;

    public ObstructionDto(Obstruction obstruction) {
        this.obstructionId = obstruction.getId();
        this.name = obstruction.getName();
    }
}
