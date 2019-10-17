package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final Meal NEW_USER_DINNER = new Meal(null, LocalDateTime.of(2015, Month.MAY, 30, 20, 0, 0), "new-dinner-user", 1000);
    private static int TEST_ID = 100001;
    public static final Meal MEAL_USER_BREAKFAST_1 = new Meal(++TEST_ID, LocalDateTime.of(2015, Month.MAY, 30, 10, 0, 0), "breakfast-user_1", 1000);
    public static final Meal MEAL_USER_LUNCH_1 = new Meal(++TEST_ID, LocalDateTime.of(2015, Month.MAY, 30, 13, 0, 0), "lunch-user_1", 1000);
    public static final Meal MEAL_USER_DINNER_1 = new Meal(++TEST_ID, LocalDateTime.of(2015, Month.MAY, 30, 19, 0, 0), "dinner-user_1", 1000);
    public static final Meal MEAL_USER_BREAKFAST_2 = new Meal(++TEST_ID, LocalDateTime.of(2015, Month.MAY, 31, 10, 0, 0), "breakfast-user_2", 1000);
    public static final Meal MEAL_USER_LUNCH_2 = new Meal(++TEST_ID, LocalDateTime.of(2015, Month.MAY, 31, 13, 0, 0), "lunch-user_2", 1000);
    public static final Meal MEAL_USER_DINNER_2 = new Meal(++TEST_ID, LocalDateTime.of(2015, Month.MAY, 31, 19, 0, 0), "dinner-user_2", 1000);
    public static final Meal MEAL_ADMIN_BREAKFAST_1 = new Meal(++TEST_ID, LocalDateTime.of(2015, Month.MAY, 30, 10, 0, 0), "breakfast-admin_1", 1000);
    public static final Meal MEAL_ADMIN_LUNCH_1 = new Meal(++TEST_ID, LocalDateTime.of(2015, Month.MAY, 30, 13, 0, 0), "lunch-admin_1", 1000);
    public static final Meal MEAL_ADMIN_DINNER_1 = new Meal(++TEST_ID, LocalDateTime.of(2015, Month.MAY, 30, 19, 0, 0), "dinner-admin_1", 1000);
    public static final Meal MEAL_ADMIN_BREAKFAST_2 = new Meal(++TEST_ID, LocalDateTime.of(2015, Month.MAY, 31, 10, 0, 0), "breakfast-admin_2", 1000);
    public static final Meal MEAL_ADMIN_LUNCH_2 = new Meal(++TEST_ID, LocalDateTime.of(2015, Month.MAY, 31, 13, 0, 0), "lunch-admin_2", 1000);
    public static final Meal MEAL_ADMIN_DINNER_2 = new Meal(++TEST_ID, LocalDateTime.of(2015, Month.MAY, 31, 19, 0, 0), "dinner-admin_2", 1000);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingOnlyGivenFields(expected, "id", "dateTime", "description", "calories");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorOnFields("id", "dateTime", "description", "calories").isEqualTo(expected);
    }
}