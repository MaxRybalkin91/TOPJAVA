package ru.javawebinar.topjava.util;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static int authUserId;

    public static void setAuthUserId(int authUserId) {
        SecurityUtil.authUserId = authUserId;
    }

    public static int getAuthUserId() {
        return authUserId;
    }

    public static int getAuthUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}