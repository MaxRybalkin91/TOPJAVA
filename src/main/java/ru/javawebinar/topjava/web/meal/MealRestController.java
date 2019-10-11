package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import static java.util.Objects.isNull;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class MealRestController {

    @Autowired
    private MealService service;

    public Collection<MealTo> getAll() {
        return service.getAll(SecurityUtil.getAuthUserId(), SecurityUtil.getAuthUserCaloriesPerDay());
    }

    public Meal create(Meal meal) {
        return service.create(SecurityUtil.getAuthUserId(), meal);
    }

    public boolean delete(int mealId) {
        return service.delete(SecurityUtil.getAuthUserId(), mealId);
    }

    public Meal get(int mealId) {
        return service.get(SecurityUtil.getAuthUserId(), mealId);
    }

    public Collection<MealTo> getAllFiltered(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        LocalDate firstDate = isNull(startDate) ? LocalDate.MIN : startDate;
        LocalTime firstTime = isNull(startTime) ? LocalTime.MIN : startTime;
        LocalDate secondDate = isNull(endDate) ? LocalDate.MAX : endDate;
        LocalTime secondTime = isNull(endTime) ? LocalTime.MAX : endTime;

        LocalDateTime start = LocalDateTime.of(firstDate, firstTime);
        LocalDateTime end = LocalDateTime.of(secondDate, secondTime);
        return service.getAllFiltered(SecurityUtil.getAuthUserId(), start, end, SecurityUtil.getAuthUserCaloriesPerDay());
    }

    public void update(int userId, Meal meal) {
        assureIdConsistent(meal, userId);
        service.update(userId, meal);
    }
}