package site.concertseat.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import site.concertseat.domain.concert.entity.Concert;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.entity.Review;
import site.concertseat.domain.review.enums.Distance;
import site.concertseat.domain.stadium.entity.Seating;

import java.util.List;

@Data
public class ReviewPostReq {
    @NotBlank
    private String stageDistance;

    @NotBlank
    private String thrustStageDistance;

    @NotBlank
    private String screenDistance;

    @NotBlank
    @Length(max = 300)
    private String contents;

    @Size(min = 1, max = 4)
    private List<String> images;

    private List<Integer> features;

    private List<Integer> obstructions;

    public Review toEntity(Member member, Seating seating, Concert concert) {
        return Review.builder()
                .member(member)
                .seating(seating)
                .concert(concert)
                .contents(contents)
                .thumbnail(images.getFirst())
                .stageDistance(Distance.valueOf(screenDistance.toUpperCase()))
                .thrustStageDistance(Distance.valueOf(thrustStageDistance.toUpperCase()))
                .screenDistance(Distance.valueOf(screenDistance.toUpperCase()))
                .isApproved(false)
                .build();
    }
}
