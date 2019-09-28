package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MapStorage implements Storage {
    private Map<Integer, Meal> mealMapStorage = new LinkedHashMap<>();
    private AtomicInteger id = new AtomicInteger(0);

    @Override
    public void save(Meal meal) {
        Integer keyId = id.getAndIncrement();
        mealMapStorage.put(keyId, meal);
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
        for (Meal meal : MealsUtil.getExampleList()) {
            mealMapStorage.put(id.getAndIncrement(), meal);
        }
    }
}