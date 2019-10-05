package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    Meal save(Integer userId, Meal meal);

    // false if not found
    boolean delete(Integer userId, Integer mealId);

    // null if not found
    Meal get(Integer userId, Integer mealId);

    Collection<Meal> getAll(Integer userId);
}