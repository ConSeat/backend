package site.concertseat.domain.stadium.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ObstructionListRes {
    private List<ObstructionDto> obstructions;
}
