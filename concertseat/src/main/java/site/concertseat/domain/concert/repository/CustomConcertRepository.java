package site.concertseat.domain.concert.repository;

import site.concertseat.domain.concert.entity.Concert;

import java.util.List;

public interface CustomConcertRepository {
    List<Concert> searchConcerts(Integer stadiumId, String[] queries);
}
