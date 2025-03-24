package site.concertseat.domain.stadium.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FeatureListRes {
    private List<FeatureDto> features;
}
