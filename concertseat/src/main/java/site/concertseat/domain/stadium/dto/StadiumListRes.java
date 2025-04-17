package site.concertseat.domain.stadium.dto;

import lombok.Data;
import site.concertseat.domain.stadium.entity.Stadium;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class StadiumListRes {
    private List<StadiumDto> active;

    private List<StadiumDto> inactive;

    private Integer totalReviewCount;

    public StadiumListRes(List<Stadium> stadiums, Integer totalReviewCount) {
        Map<Boolean, List<Stadium>> stadiumMap = stadiums.stream()
                .collect(Collectors.groupingBy(Stadium::getIsActive));

        this.active = stadiumMap.getOrDefault(true, new ArrayList<>())
                .stream()
                .map(StadiumDto::toDto)
                .toList();

        this.inactive = stadiumMap.getOrDefault(false, new ArrayList<>())
                .stream()
                .map(StadiumDto::toDto)
                .toList();

        this.totalReviewCount = totalReviewCount;
    }
}
