package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDateTime;
import java.util.Collection;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    public Collection<Meal> getAll() {
        return service.getAll();
    }

    public Meal create(Meal meal) {
        return service.create(meal);
    }

    public boolean delete(int id) {
        return service.delete(id);
    }

    public Meal get(int id) {
        return service.get(id);
    }

    public Collection<Meal> getAllFiltered(LocalDateTime start, LocalDateTime end) {
        return service.getAllFiltered(start, end);
    }
}