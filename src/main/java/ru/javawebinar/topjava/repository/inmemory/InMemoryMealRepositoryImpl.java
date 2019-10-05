package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, List<Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private static final int TEST_USER_ID = authUserId();

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            Integer id = counter.getAndIncrement();
            meal.setId(id);
        }
        setRepository(meal);
        return meal;
    }

    @Override
    public boolean delete(int id) {
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id) {
        List<Meal> mealList = getAll();
        for (Meal meal : mealList) {
            if (meal.getId() == id) {
                return meal;
            }
        }
        return null;
    }

    @Override
    public List<Meal> getAll() {
        return repository.get(TEST_USER_ID);
    }

    @Override
    public Collection<Meal> getAllFiltered(LocalDateTime start, LocalDateTime end) {
        List<Meal> mealList = new ArrayList<>();
        for (Meal meal : getAll()) {
            if (DateTimeUtil.isBetween(meal.getDateTime(), start, end)) {
                mealList.add(meal);
            }
        }
        return mealList;
    }

    private void setRepository(Meal meal) {
        List<Meal> mealList = new ArrayList<>();
        mealList.add(meal);
        repository.merge(TEST_USER_ID, mealList, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
    }
}

