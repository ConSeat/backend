package site.concertseat.domain.stadium.dto;

import lombok.Data;
import site.concertseat.domain.stadium.entity.Section;

import java.util.List;

@Data
public class SeatingListRes {
    private String sectionInfo;

    private List<SeatingWithCountDto> seating;

    public SeatingListRes(Section section, List<SeatingWithCountDto> seating) {
        this.sectionInfo = section.getFloor().getName() + " " + section.getName();
        this.seating = seating;
    }
}
