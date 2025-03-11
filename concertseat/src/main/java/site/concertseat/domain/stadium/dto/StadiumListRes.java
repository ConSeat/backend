package site.concertseat.domain.stadium.dto;

import lombok.Data;
import site.concertseat.domain.stadium.entity.Stadium;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class StadiumListRes {
    private List<StadiumDto> active;

    private List<StadiumDto> inactive;

    public StadiumListRes(List<Stadium> stadiums) {
        Map<Boolean, List<Stadium>> stadiumMap = stadiums.stream()
                .collect(Collectors.groupingBy(Stadium::getIsActive));

        this.active = stadiumMap.get(true)
                .stream()
                .map(StadiumDto::toDto)
                .toList();

        this.inactive = stadiumMap.get(false)
                .stream()
                .map(StadiumDto::toDto)
                .toList();
    }
}
