package site.concertseat.domain.stadium.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.concertseat.domain.stadium.dto.StadiumListRes;
import site.concertseat.domain.stadium.service.StadiumService;
import site.concertseat.global.dto.ResponseDto;

import static site.concertseat.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/stadiums")
@RequiredArgsConstructor
public class StadiumController {
    private final StadiumService stadiumService;

    @GetMapping
    public ResponseDto<StadiumListRes> stadiumList() {
        StadiumListRes result = stadiumService.findStadiums();

        return ResponseDto.success(OK, result);
    }
}
