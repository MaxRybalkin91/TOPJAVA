package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MapStorage implements Storage {
    private Map<Integer, Meal> mealMapStorage = new ConcurrentHashMap<>();
    private AtomicInteger id = new AtomicInteger(0);

    @Override
    public void save(Meal meal) {
        Integer mealId = meal.getId();
        if (mealId == null) {
            mealId = id.getAndIncrement();
            meal.setId(mealId);
        }
        mealMapStorage.put(mealId, meal);
    }

    public void delete(Integer id) {
        mealMapStorage.remove(id);
    }

    @Override
    public Meal get(Integer id) {
        return mealMapStorage.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return mealMapStorage.values();
    }

    public void fillMapStorage() {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        for (Meal meal : meals) {
            save(meal);
        }
    }
}