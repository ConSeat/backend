package site.concertseat.domain.review.enums;

public enum Distance {
    CLOSE(0),
    AVERAGE(1),
    FAR(2),
    ;

    private Integer distance;

    Distance(Integer distance) {
        this.distance = distance;
    }
}
