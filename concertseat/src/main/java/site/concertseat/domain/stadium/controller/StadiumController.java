package site.concertseat.domain.stadium.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.concertseat.domain.concert.dto.ConcertSearchReq;
import site.concertseat.domain.concert.dto.ConcertSearchRes;
import site.concertseat.domain.concert.service.ConcertService;
import site.concertseat.domain.stadium.dto.FeatureListRes;
import site.concertseat.domain.stadium.dto.StadiumDetailsRes;
import site.concertseat.domain.stadium.dto.StadiumListRes;
import site.concertseat.domain.stadium.service.StadiumService;
import site.concertseat.global.dto.ResponseDto;

import static site.concertseat.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/stadiums")
@RequiredArgsConstructor
public class StadiumController {
    private final StadiumService stadiumService;
    private final ConcertService concertService;

    @GetMapping
    public ResponseDto<StadiumListRes> stadiumList() {
        StadiumListRes result = stadiumService.findStadiums();

        return ResponseDto.success(OK, result);
    }

    @GetMapping("/{stadiumId}")
    public ResponseDto<StadiumDetailsRes> stadiumDetails(@PathVariable Integer stadiumId) {
        StadiumDetailsRes result = stadiumService.getStadiumDetails(stadiumId);

        return ResponseDto.success(OK, result);
    }

    @GetMapping("/{stadiumId}/concerts")
    public ResponseDto<ConcertSearchRes> concertSearch(@PathVariable Integer stadiumId,
                                                       @ModelAttribute ConcertSearchReq concertSearchReq) {
        ConcertSearchRes result = concertService.concertSearch(stadiumId, concertSearchReq);

        return ResponseDto.success(OK, result);
    }

    @GetMapping("/features")
    public ResponseDto<FeatureListRes> featureList() {
        FeatureListRes result = stadiumService.findFeatures();

        return ResponseDto.success(OK, result);
    }
}
