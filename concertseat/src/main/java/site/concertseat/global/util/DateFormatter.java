package site.concertseat.global.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static String calculateTime(LocalDateTime date) {
        Duration duration = Duration.between(date, LocalDateTime.now());
        long minutes = duration.toMinutes();

        if (minutes < TIME_MAXIMUM.MINUTE) {
            return minutes + "분 전";
        } else if ((minutes /= TIME_MAXIMUM.MINUTE) < TIME_MAXIMUM.HOUR) {
            return minutes + "시간 전";
        } else if ((minutes /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            return minutes + "일 전";
        }

        return date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    private static class TIME_MAXIMUM {
        public static final int MINUTE = 60;
        public static final int HOUR = 24;
        public static final int DAY = 7;
    }
}