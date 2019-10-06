package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDateTime).reversed();
    private List emptyList = Collections.EMPTY_LIST;

    {
        MealsUtil.MEALS.forEach(meal -> save(1, meal));
        MealsUtil.MEALS.forEach(meal -> save(2, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            int id = counter.getAndIncrement();
            meal.setId(id);
        }
        Map<Integer, Meal> mealMap = new HashMap<>();
        mealMap.put(meal.getId(), meal);
        repository.merge(userId, mealMap, (map1, map2) -> {
            map1.putAll(map2);
            return map1;
        });
        return meal;
    }

    @Override
    public boolean delete(int userId, int mealId) {
        log.info("delete meal_id {} of user_id {}", mealId, userId);
        return repository.get(userId).remove(mealId) != null;
    }

    @Override
    public Meal get(int userId, int mealId) {
        log.info("get meal_id {} of user_id {}", mealId, userId);
        return repository.get(userId).get(mealId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("get all of user_id {}", userId);
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) {
            return emptyList;
        }
        return getFiltered(mealMap.values(), meal -> true);
    }

    @Override
    public List<Meal> getAllFiltered(int userId, LocalDateTime start, LocalDateTime end) {
        log.info("get all filtered of user_id {} between dateTimes {} and {}", userId, start, end);
        return getFiltered(getAll(userId), meal -> DateTimeUtil.isBetween(meal.getDateTime().toLocalDate(), start.toLocalDate(), end.toLocalDate()));
    }

    private List<Meal> getFiltered(Collection<Meal> mealList, Predicate<Meal> filter) {
        return mealList.stream().filter(filter).sorted(MEAL_COMPARATOR).collect(Collectors.toList());
    }

}