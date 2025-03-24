package site.concertseat.domain.stadium.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.concertseat.domain.review.repository.FeatureRepository;
import site.concertseat.domain.review.repository.ObstructionRepository;
import site.concertseat.domain.stadium.dto.*;
import site.concertseat.domain.stadium.entity.Floor;
import site.concertseat.domain.stadium.entity.Seating;
import site.concertseat.domain.stadium.entity.Section;
import site.concertseat.domain.stadium.entity.Stadium;
import site.concertseat.domain.stadium.repository.StadiumRepository;
import site.concertseat.global.exception.CustomException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static site.concertseat.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {
    private final StadiumRepository stadiumRepository;
    private final FeatureRepository featureRepository;
    private final ObstructionRepository obstructionRepository;

    @Override
    public StadiumListRes findStadiums() {
        List<Stadium> stadiums = stadiumRepository.findAll();

        return new StadiumListRes(stadiums);
    }

    @Override
    public StadiumDetailsRes getStadiumDetails(Integer stadiumId) {
        if (!stadiumRepository.existsById(stadiumId)) {
            throw new CustomException(NOT_FOUND);
        }

        List<Floor> floors = stadiumRepository.findFloorsByStadiumId(stadiumId);

        List<Section> sectionsByFloors = stadiumRepository.findSectionsByFloors(floors);

        Map<Integer, List<Section>> sections = sectionsByFloors.stream()
                .collect(Collectors.groupingBy(section -> section.getFloor().getId()));

        Map<Integer, List<Seating>> seating = stadiumRepository.findSeatingBySections(sectionsByFloors).stream()
                .collect(Collectors.groupingBy(seats -> seats.getSection().getId()));

        return new StadiumDetailsRes(floors, sections, seating);
    }

    @Override
    public FeatureListRes findFeatures() {
        return new FeatureListRes(featureRepository.findAllFeatures().stream()
                .map(FeatureDto::new)
                .toList());
    }

    @Override
    public ObstructionListRes findObstructions() {
        return new ObstructionListRes(obstructionRepository.findAllObstructions().stream()
                .map(ObstructionDto::new)
                .toList());
    }
}
