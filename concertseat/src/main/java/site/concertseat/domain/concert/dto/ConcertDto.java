package site.concertseat.domain.concert.dto;

import lombok.Builder;
import lombok.Data;
import site.concertseat.domain.concert.entity.Concert;

@Data
@Builder
public class ConcertDto {
    private Integer concertId;

    private String concertName;

    public static ConcertDto toDto(Concert concert) {
        return ConcertDto.builder()
                .concertId(concert.getId())
                .concertName(concert.getName())
                .build();
    }
}
