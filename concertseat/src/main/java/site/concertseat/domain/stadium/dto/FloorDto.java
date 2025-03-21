package site.concertseat.domain.stadium.dto;

import lombok.Data;
import site.concertseat.domain.stadium.entity.Floor;
import site.concertseat.domain.stadium.entity.Seating;
import site.concertseat.domain.stadium.entity.Section;

import java.util.List;
import java.util.Map;

@Data
public class FloorDto {
    private String name;

    private List<SectionDto> sections;

    public FloorDto(Floor floor, List<Section> sections, Map<Integer, List<Seating>> seating) {
        this.name = floor.getName();
        this.sections = sections.stream()
                .map(section -> new SectionDto(section, seating.get(section.getId())))
                .toList();
    }
}
