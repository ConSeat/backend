package site.concertseat.domain.stadium.dto;

import lombok.Data;
import site.concertseat.domain.stadium.entity.Floor;
import site.concertseat.domain.stadium.entity.Section;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class SectionListRes {
    private List<FloorSectionDto> floors;

    public SectionListRes(List<Floor> floors, List<Section> sections) {
        Map<Integer, List<Section>> sectionGroupedByFloor = sections.stream()
                .collect(Collectors.groupingBy(section -> section.getFloor().getId()));

        this.floors = floors.stream()
                .map(floor -> new FloorSectionDto(floor, sectionGroupedByFloor.get(floor.getId())))
                .toList();
    }
}
