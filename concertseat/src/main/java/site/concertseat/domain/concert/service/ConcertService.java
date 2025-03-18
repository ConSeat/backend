package site.concertseat.domain.concert.service;

import site.concertseat.domain.concert.dto.ConcertSearchReq;
import site.concertseat.domain.concert.dto.ConcertSearchRes;

public interface ConcertService {
    ConcertSearchRes concertSearch(Integer stadiumId, ConcertSearchReq concertSearchReq);
}
