package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private Map<Integer, List<Integer>> userMealIdMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Integer userId, Meal meal) {
        if (meal.isNew()) {
            Integer id = counter.getAndIncrement();
            meal.setId(id);
            repository.put(id, meal);
            setUserKeysMap(userId, id);
            return meal;
        }
        setUserKeysMap(userId, meal.getId());
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(Integer userId, Integer mealId) {
        if (isMealOfUser(userId, mealId)) {
            return repository.remove(mealId) != null;
        }
        return false;
    }

    @Override
    public Meal get(Integer userId, Integer mealId) {
        if (isMealOfUser(userId, mealId)) {
            return repository.get(mealId);
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        List<Meal> mealList = new ArrayList<>();
        List<Integer> userMealIdList = userMealIdMap.get(userId);
        for (Integer mealId : userMealIdList) {
            mealList.add(repository.get(mealId));
        }
        return mealList;
    }

    public void fillStorage(Integer userId) {
        List<Meal> testList = MealsUtil.MEALS;
        for (Meal meal : testList) {
            save(userId, meal);
        }
    }

    private void setUserKeysMap(Integer userId, Integer mealId) {
        List<Integer> mealIdList = new ArrayList<>();
        mealIdList.add(mealId);
        userMealIdMap.merge(userId, mealIdList, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
    }

    private boolean isMealOfUser(Integer userId, Integer mealId) {
        List<Integer> userMealIdList = userMealIdMap.get(userId);
        return userMealIdList.contains(mealId);
    }
}

