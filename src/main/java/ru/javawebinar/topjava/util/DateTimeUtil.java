package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetween(T dateOrTime, T start, T end) {
        return dateOrTime.compareTo(start) >= 0 && dateOrTime.compareTo(end) <= 0;
    }

    public static List<LocalDateTime> getParsed(String startDate, String startTime, String endDate, String endTime) {
        LocalDate firstDate = isEmpty(startDate) ? LocalDate.MIN : LocalDate.parse(startDate);
        LocalTime firstTime = isEmpty(startTime) ? LocalTime.MIN : LocalTime.parse(startTime);
        LocalDate secondDate = isEmpty(endDate) ? LocalDate.MAX : LocalDate.parse(endDate);
        LocalTime secondTime = isEmpty(endTime) ? LocalTime.MAX : LocalTime.parse(endTime);

        LocalDateTime start = LocalDateTime.of(firstDate, firstTime);
        LocalDateTime end = LocalDateTime.of(secondDate, secondTime);
        return Arrays.asList(start, end);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    private static boolean isEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }
}
