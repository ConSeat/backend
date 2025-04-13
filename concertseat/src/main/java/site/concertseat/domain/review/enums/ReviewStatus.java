package site.concertseat.domain.review.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ReviewStatus {
    WAITING("심사대기"),
    APPROVED("승인"),
    REJECTED("반려"),
    RE_REVIEW("재심사");

    private final String description;

    ReviewStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
