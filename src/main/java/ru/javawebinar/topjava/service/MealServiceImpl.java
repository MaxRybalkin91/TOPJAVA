package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private final MealRepository repository;

    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public boolean delete(Integer mealId) throws NotFoundException {
        return checkNotFoundWithId(repository.delete(mealId), mealId);
    }

    @Override
    public Meal get(Integer mealId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(mealId), mealId);
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.getAll();
    }

    @Override
    public Collection<Meal> getAllFiltered(LocalDateTime start, LocalDateTime end) throws NotFoundException {
        return repository.getAllFiltered(start, end);
    }
}