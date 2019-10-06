package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private final MealRepository repository;

    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(int userId, Meal meal) {
        return repository.save(userId, meal);
    }

    @Override
    public boolean delete(int userId, int mealId) throws NotFoundException {
        return checkNotFoundWithId(repository.delete(userId, mealId), mealId);
    }

    @Override
    public Meal get(int userId, int mealId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(userId, mealId), mealId);
    }

    @Override
    public Collection<MealTo> getAll(int userId, int caloriesPerDay) {
        return MealsUtil.getWithExcess(repository.getAll(userId), caloriesPerDay);
    }

    @Override
    public Collection<MealTo> getAllFiltered(int userId, LocalDateTime start, LocalDateTime end, int caloriesPerDay) throws NotFoundException {
        return MealsUtil.getWithExcess(repository.getAllFiltered(userId, start, end), caloriesPerDay)
                .stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime().toLocalTime(), start.toLocalTime(), end.toLocalTime()))
                .collect(Collectors.toList());
    }
}