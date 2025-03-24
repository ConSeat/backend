package site.concertseat.domain.stadium.service;

import site.concertseat.domain.stadium.dto.FeatureListRes;
import site.concertseat.domain.stadium.dto.StadiumDetailsRes;
import site.concertseat.domain.stadium.dto.StadiumListRes;

public interface StadiumService {
    StadiumListRes findStadiums();

    StadiumDetailsRes getStadiumDetails(Integer stadiumId);

    FeatureListRes findFeatures();
}
