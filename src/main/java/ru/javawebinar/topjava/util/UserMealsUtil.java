package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        List<UserMealWithExceed> filteredList = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExceed> filteredStreamList = getFilteredStreamWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        printUsers(filteredList);
        printUsers(filteredStreamList);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesOfDayMap = new HashMap<>();
        List<UserMealWithExceed> userList = new ArrayList<>();

        for (UserMeal um : mealList) {
            Integer calories = um.getCalories();
            caloriesOfDayMap.merge(um.getDateTime().toLocalDate(), calories, Integer::sum);
        }

        for (UserMeal um : mealList) {

            if (TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime)) {
                userList.add(createUserMealWithExceed(caloriesOfDayMap, um, caloriesPerDay));
            }
        }

        return userList;
    }

    public static List<UserMealWithExceed> getFilteredStreamWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesOfDayMap =
                mealList.stream().collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        return mealList.stream()
                .filter(um -> TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime))
                .map(um -> createUserMealWithExceed(caloriesOfDayMap, um, caloriesPerDay))
                .collect(Collectors.toList());
    }


    private static UserMealWithExceed createUserMealWithExceed(Map<LocalDate, Integer> caloriesOfDayMap, UserMeal um, int caloriesPerDay) {
        boolean isExceeds = caloriesOfDayMap.get(um.getDateTime().toLocalDate()) >= caloriesPerDay;
        return new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(), isExceeds);
    }

    private static void printUsers(List<UserMealWithExceed> list) {
        for (UserMealWithExceed um : list) {
            System.out.println(um.printUser());
        }
    }
}