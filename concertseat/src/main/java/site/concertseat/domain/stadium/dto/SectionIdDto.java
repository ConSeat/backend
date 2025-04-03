package site.concertseat.domain.stadium.dto;

import lombok.Data;
import site.concertseat.domain.stadium.entity.Section;

@Data
public class SectionIdDto {
    private Integer sectionId;

    private String name;

    public SectionIdDto(Section section) {
        this.sectionId = section.getId();
        this.name = section.getName();
    }
}
