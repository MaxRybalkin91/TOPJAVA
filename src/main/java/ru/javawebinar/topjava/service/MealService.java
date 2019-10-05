package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;

public interface MealService {

    Meal create(Meal meal);

    boolean delete(Integer mealId) throws NotFoundException;

    Meal get(Integer mealId) throws NotFoundException;

    Collection<Meal> getAll();

    Collection<Meal> getAllFiltered(LocalDateTime start, LocalDateTime end);
}