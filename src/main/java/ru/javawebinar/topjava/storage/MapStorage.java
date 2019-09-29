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
        int id = incrementId();
        if (isEmptyId(meal)) {
            meal.setId(id);
        }
        mealMapStorage.put(id, meal);
    }

    @Override
    public void update(Integer id, Meal meal) {
        mealMapStorage.replace(id, meal);
    }

    @Override
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
        mealMapStorage.clear();
        List<Meal> meals = Arrays.asList(
                new Meal(incrementId(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(incrementId(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(incrementId(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(incrementId(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(incrementId(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(incrementId(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        for (Meal meal : meals) {
            mealMapStorage.put(meal.getId(), meal);
        }
    }

    private int incrementId() {
        return id.getAndIncrement();
    }

    private boolean isEmptyId(Meal meal) {
        return meal.getId() == null;
    }
}