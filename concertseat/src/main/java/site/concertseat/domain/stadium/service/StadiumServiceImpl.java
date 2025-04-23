package site.concertseat.domain.stadium.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.concertseat.domain.review.repository.FeatureRepository;
import site.concertseat.domain.review.repository.ObstructionRepository;
import site.concertseat.domain.review.service.ReviewService;
import site.concertseat.domain.stadium.dto.*;
import site.concertseat.domain.stadium.entity.Floor;
import site.concertseat.domain.stadium.entity.Seating;
import site.concertseat.domain.stadium.entity.Section;
import site.concertseat.domain.stadium.entity.Stadium;
import site.concertseat.domain.stadium.repository.StadiumRepository;
import site.concertseat.global.exception.CustomException;
import site.concertseat.global.redis.RedisUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static site.concertseat.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {
    private static final String STADIUM_KEY = "stadium";
    private static final String REVIEW_COUNT_KEY = "reviewCount";
    private final StadiumRepository stadiumRepository;
    private final FeatureRepository featureRepository;
    private final ObstructionRepository obstructionRepository;
    private final ReviewService reviewService;
    private final RedisUtils redisUtils;

    @Override
    public StadiumListRes findStadiums() {
        List<Stadium> stadiums;
        Integer totalReviewCount;

        Object stadiumList = redisUtils.getData(STADIUM_KEY);
        if(stadiumList == null) {
            stadiums = stadiumRepository.findAll();
            redisUtils.setData("stadium", stadiums);
        } else {
            stadiums = (List<Stadium>) stadiumList;
        }

        Object reviewCount = redisUtils.getData(REVIEW_COUNT_KEY);
        if(reviewCount == null) {
            totalReviewCount = reviewService.getTotalReviewCount();
            redisUtils.setData("reviewCount", totalReviewCount);
        } else {
            totalReviewCount = (Integer) reviewCount;
        }

        return new StadiumListRes(stadiums, totalReviewCount);
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
    public SectionListRes findSections(Integer stadiumId) {
        List<Floor> floors = stadiumRepository.findFloorsByStadiumId(stadiumId);
        List<Section> sections = stadiumRepository.findSectionsByFloors(floors);

        return new SectionListRes(floors, sections);
    }

    @Override
    public SeatingListRes findSeating(Integer sectionId) {
        Section section = stadiumRepository.findSectionWithFloor(sectionId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        List<SeatingWithCountDto> seatingWithCount = stadiumRepository.findSeatingWithCount(sectionId);

        return new SeatingListRes(section, seatingWithCount);
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
