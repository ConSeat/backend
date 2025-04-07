package site.concertseat.domain.stadium.dto;

import lombok.Data;
import site.concertseat.domain.stadium.entity.Floor;
import site.concertseat.domain.stadium.entity.Section;

import java.util.List;

@Data
public class FloorSectionDto {
    private String name;

    private List<SectionIdDto> sections;

    public FloorSectionDto(Floor floor, List<Section> sections) {
        this.name = floor.getName();
        this.sections = sections.stream()
                .map(SectionIdDto::new)
                .toList();
    }
}
