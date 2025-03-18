package site.concertseat.domain.concert.dto;

import lombok.Data;
import site.concertseat.domain.concert.entity.Concert;

import java.util.List;

@Data
public class ConcertSearchRes {
    private List<ConcertDto> concerts;

    public ConcertSearchRes(List<Concert> concerts) {
        this.concerts = concerts.stream()
                .map(ConcertDto::toDto)
                .toList();
    }
}
