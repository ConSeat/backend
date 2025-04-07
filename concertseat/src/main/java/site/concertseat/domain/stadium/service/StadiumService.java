package site.concertseat.domain.stadium.service;

import site.concertseat.domain.stadium.dto.*;

public interface StadiumService {
    StadiumListRes findStadiums();

    StadiumDetailsRes getStadiumDetails(Integer stadiumId);

    SectionListRes findSections(Integer stadiumId);

    SeatingListRes findSeating(Integer sectionId);

    FeatureListRes findFeatures();

    ObstructionListRes findObstructions();
}
