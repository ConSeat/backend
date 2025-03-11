package site.concertseat.domain.stadium.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.concertseat.domain.stadium.dto.StadiumListRes;
import site.concertseat.domain.stadium.entity.Stadium;
import site.concertseat.domain.stadium.repository.StadiumRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {
    private final StadiumRepository stadiumRepository;

    @Override
    public StadiumListRes findStadiums() {
        List<Stadium> stadiums = stadiumRepository.findAll();

        return new StadiumListRes(stadiums);
    }
}
