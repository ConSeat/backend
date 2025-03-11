package site.concertseat.domain.review.dto;

import lombok.Data;

@Data
public class ReviewStatsDto {
    private static final String[] messages = new String[]{
            "본무대, 돌출, 전광판 모두 잘 보여요",
            "본무대와 돌출이 가까우며, 전광판은 보통이에요",
            "본무대와 돌출이 가깝지만, 전광판이 안 보여요",
            "본무대가 가깝고, 돌출은 보통이며, 전광판도 잘 보여요",
            "본무대가 가까우며, 돌출과 전광판은 보통이에요",
            "본무대가 가까우며, 돌출은 보통이지만, 전광판이 안 보여요",
            "본무대가 가깝고 전광판이 잘 보이지만, 돌출이 멀어요",
            "본무대는 가깝지만, 돌출이 멀고, 전광판은 보통이에요",
            "본무대는 가깝지만, 돌출이 멀고 전광판이 안 보여요",
            "본무대는 보통이며, 돌출이 가까워요. 전광판도 잘 보여요",
            "본무대는 보통이며, 돌출이 가까워요. 전광판은 보통이에요",
            "본무대는 보통이며, 돌출이 가까우나, 전광판이 안 보여요",
            "본무대, 돌출은 보통이며, 전광판이 잘 보여요",
            "본무대, 돌출, 전광판 모두 보통이에요",
            "본무대, 돌출은 보통이며, 전광판이 안 보여요",
            "본무대는 보통이며, 돌출이 멀지만, 전광판이 잘 보여요",
            "본무대는 보통이며, 돌출이 멀고, 전광판은 보통이에요",
            "본무대는 보통이며, 돌출이 멀고, 전광판이 안 보여요",
            "본무대는 멀지만, 돌출이 가까워요. 전광판도 잘 보여요",
            "본무대는 멀지만, 돌출이 가까워요. 전광판은 보통이에요",
            "본무대는 멀지만, 돌출이 가까우나, 전광판이 안 보여요",
            "본무대는 멀지만, 돌출은 보통이고, 전광판이 잘 보여요",
            "본무대는 멀지만, 돌출과 전광판은 보통이에요",
            "본무대는 멀고 전광판만 안 보이지만, 돌출은 보통이에요",
            "본무대와 돌출이 멀지만, 전광판은 잘 보여요",
            "본무대와 돌출이 멀고, 전광판은 보통이에요",
            "본무대, 돌출, 전광판 모두 멀거나 안 보여요"
    };

    private int stageDistance;

    private int thrustStageDistance;

    private int screenDistance;

    private String message;

    private Long reviewCount;

    public ReviewStatsDto(Double stageDistance, Double thrustStageDistance, Double screenDistance, Long reviewCount) {
        this.stageDistance = toInt(stageDistance);
        this.thrustStageDistance = toInt(thrustStageDistance);
        this.screenDistance = toInt(screenDistance);
        this.message = messages[9 * this.stageDistance + 3 * this.thrustStageDistance + this.screenDistance];
        this.reviewCount = reviewCount;
    }

    private int toInt(double d) {
        return Math.min((int) (1.5 * d), 2);
    }
}
