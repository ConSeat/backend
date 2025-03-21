package site.concertseat.domain.stadium.dto;

import lombok.Data;
import site.concertseat.domain.stadium.entity.Seating;
import site.concertseat.domain.stadium.entity.Section;

import java.util.List;

@Data
public class SectionDto {
    private String name;

    private List<SeatingDto> seats;

    public SectionDto(Section section,List<Seating> seats) {
        this.name = section.getName();
        this.seats = seats.stream()
                .map(SeatingDto::new)
                .toList();
    }
}
