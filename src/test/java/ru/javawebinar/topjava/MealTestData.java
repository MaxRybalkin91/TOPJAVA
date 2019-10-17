package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final Meal MEAL_1 = new Meal(100000, LocalDateTime.of(2015, Month.MAY, 30, 10, 0, 0), "breakfast-user", 1000);
    public static final Meal MEAL_2 = new Meal(99990, LocalDateTime.of(2015, Month.MAY, 30, 13, 0, 0), "lunch-user", 1000);
    public static final Meal MEAL_3 = new Meal(99980, LocalDateTime.of(2015, Month.MAY, 30, 10, 0, 0), "breakfast-admin", 1000);
    public static final Meal MEAL_4 = new Meal(99970, LocalDateTime.of(2015, Month.MAY, 30, 13, 0, 0), "lunch-admin", 1000);
    public static final Meal MEAL_5 = new Meal(null, LocalDateTime.of(2015, Month.MAY, 30, 19, 0, 0), "dinner-user", 1000);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}