package site.concertseat.domain.stadium.dto;

import lombok.Builder;
import lombok.Data;
import site.concertseat.domain.stadium.entity.Stadium;

@Data
@Builder
public class StadiumDto {
    private Integer stadiumId;

    private String stadiumName;

    private String stadiumImage;

    public static StadiumDto toDto(Stadium stadium) {
        return StadiumDto.builder()
                .stadiumId(stadium.getId())
                .stadiumName(stadium.getName())
                .stadiumImage(stadium.getImage())
                .build();
    }
}
