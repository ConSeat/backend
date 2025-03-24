package site.concertseat.domain.stadium.dto;

import lombok.Data;
import site.concertseat.domain.review.entity.Feature;

@Data
public class FeatureDto {
    private Integer featureId;

    private String name;

    public FeatureDto(Feature feature) {
        this.featureId = feature.getId();
        this.name = feature.getName();
    }
}
