package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface Storage {

    public void save(Meal meal);

    public void delete(Integer id);

    public void update(Integer id, Meal meal);

    public Meal get(Integer id);

    public Collection<Meal> getAll();
}