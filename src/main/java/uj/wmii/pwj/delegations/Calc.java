package uj.wmii.pwj.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Calc {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm VV");

    public BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate)
            throws IllegalArgumentException {

        ZonedDateTime startDate;
        ZonedDateTime endDate;
        try {
            startDate = ZonedDateTime.parse(start, FORMATTER);
            endDate = ZonedDateTime.parse(end, FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("bledny format", e);
        }

        if (!endDate.isAfter(startDate)) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        Duration duration = Duration.between(startDate, endDate);
        long min = duration.toMinutes();

        long days = min / (24 * 60);

        long remainingMinutes = min % (24 * 60);

        BigDecimal result = dailyRate.multiply(BigDecimal.valueOf(days));

        if (remainingMinutes > 0) {
            if (remainingMinutes <= 8 * 60) {
                result = result.add(dailyRate.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP));
            } else if (remainingMinutes <= 12 * 60) {
                result = result.add(dailyRate.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
            } else {
                result = result.add(dailyRate);
            }
        }

        return result.setScale(2, RoundingMode.HALF_UP);
    }
}