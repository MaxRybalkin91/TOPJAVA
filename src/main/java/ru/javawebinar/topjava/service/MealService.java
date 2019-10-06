package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;

public interface MealService {

    Meal create(int userId, Meal meal);

    boolean delete(int userId, int mealId) throws NotFoundException;

    Meal get(int userId, int mealId) throws NotFoundException;

    Collection<MealTo> getAll(int userId, int caloriesPerDay);

    Collection<MealTo> getAllFiltered(int userId, LocalDateTime start, LocalDateTime end, int caloriesPerDay) throws NotFoundException;
}