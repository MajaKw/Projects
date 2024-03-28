package uj.wmii.pwj.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Calc {
    BigDecimal getPayRate(long hours, BigDecimal dailyRate) {
        if (hours < 8) return dailyRate.divide(new BigDecimal(3), 2, RoundingMode.HALF_UP);
        else if (hours > 12) {
            return dailyRate;
        } else return dailyRate.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);
    }

    BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
        ZonedDateTime startDate = ZonedDateTime.parse(start, formatter);
        ZonedDateTime endDate = ZonedDateTime.parse(end, formatter);

        if (!startDate.isBefore(endDate)) return new BigDecimal("0.00");

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        long hours = ChronoUnit.HOURS.between(startDate, endDate) % 24;

        if (days == 1) return dailyRate;
        return getPayRate(hours, dailyRate).add(dailyRate.multiply(new BigDecimal(days)));
    }

    public static void main(String[] args) {
    }
}
