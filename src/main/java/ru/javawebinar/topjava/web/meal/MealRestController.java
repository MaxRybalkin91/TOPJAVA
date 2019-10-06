package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Collection;

@Controller
public class MealRestController {

    @Autowired
    private MealService service;

    public Collection<MealTo> getAll() {
        return service.getAll(getUserId(), getUserCalories());
    }

    public Meal create(Meal meal) {
        return service.create(getUserId(), meal);
    }

    public boolean delete(int mealId) {
        return service.delete(getUserId(), mealId);
    }

    public Meal get(int mealId) {
        return service.get(getUserId(), mealId);
    }

    public Collection<MealTo> getAllFiltered(LocalDateTime start, LocalDateTime end) {
        return service.getAllFiltered(getUserId(), start, end, getUserCalories());
    }

    private int getUserId() {
        return SecurityUtil.getAuthUserId();
    }

    private int getUserCalories() {
        return SecurityUtil.getAuthUserCaloriesPerDay();
    }
}