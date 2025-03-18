package site.concertseat.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.concertseat.domain.concert.dto.ConcertSearchReq;
import site.concertseat.domain.concert.dto.ConcertSearchRes;
import site.concertseat.domain.concert.entity.Concert;
import site.concertseat.domain.concert.repository.CustomConcertRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConcertServiceImpl implements ConcertService {
    private final CustomConcertRepository customConcertRepository;

    @Override
    public ConcertSearchRes concertSearch(Integer stadiumId, ConcertSearchReq concertSearchReq) {
        String[] queries = concertSearchReq
                .getQuery()
                .strip()
                .split("\\s+");

        List<Concert> concerts = customConcertRepository.searchConcerts(stadiumId, queries);

        return new ConcertSearchRes(concerts);
    }
}
