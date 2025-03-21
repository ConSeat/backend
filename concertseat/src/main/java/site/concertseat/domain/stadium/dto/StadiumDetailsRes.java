package site.concertseat.domain.stadium.dto;

import lombok.Data;
import site.concertseat.domain.stadium.entity.Floor;
import site.concertseat.domain.stadium.entity.Seating;
import site.concertseat.domain.stadium.entity.Section;

import java.util.List;
import java.util.Map;

@Data
public class StadiumDetailsRes {
    private List<FloorDto> floors;

    public StadiumDetailsRes(List<Floor> floors,
                             Map<Integer, List<Section>> sections,
                             Map<Integer, List<Seating>> seating) {
        this.floors = floors.stream()
                .map(floor -> new FloorDto(floor, sections.get(floor.getId()), seating))
                .toList();
    }
}
