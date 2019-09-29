package ru.javawebinar.topjava.model;

public class User {
    //Temporary static, will be non static soon
    private static int caloriesPerDay;

    public static int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public static void setCaloriesPerDay(int caloriesPerDay) {
        User.caloriesPerDay = caloriesPerDay;
    }
}
