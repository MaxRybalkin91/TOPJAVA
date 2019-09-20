package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMealWithExceed {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean exceed;

    public UserMealWithExceed(LocalDateTime dateTime, String description, int calories, boolean exceed) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.exceed = exceed;
    }

    public String printUser() {
        return appendLines(dateTime.toString(), description, "" + calories, "" + exceed);
    }

    private String appendLines(String... values) {
        StringBuilder sb = new StringBuilder();
        for (String s : values) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }
}
