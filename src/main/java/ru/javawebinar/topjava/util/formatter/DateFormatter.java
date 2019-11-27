package ru.javawebinar.topjava.util.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

public class DateFormatter implements Formatter<LocalDate> {
    @Override
    public String print(LocalDate date, Locale locale) {
        return date.toString();
    }

    @Override
    public LocalDate parse(String s, Locale locale) throws ParseException {
        return s != null && !s.isEmpty() ? LocalDate.parse(s) : null;
    }
}
