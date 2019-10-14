package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() throws Exception {
        Meal newMeal = MEAL_3;
        Meal created = service.create(newMeal, USER_ID);
        int mealId = created.getId();
        newMeal.setId(mealId);
        assertMatch(service.get(mealId, USER_ID), newMeal);
    }

    @Test
    public void get() {
        Meal meal = service.get(START_SEQ, USER_ID);
        assertMatch(meal, MEAL_1);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), MEAL_2, MEAL_1);
    }

    @Test
    public void delete() {
        service.delete(START_SEQ, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL_2);
    }

    @Test
    public void update() {
        service.update(MEAL_1, USER_ID);
        assertMatch(service.get(START_SEQ, USER_ID), MEAL_1);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateCreate() throws Exception {
        MEAL_1.setId(null);
        service.create(MEAL_1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAlienMeal() {
        service.delete(START_SEQ, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateAlienMeal() {
        service.update(MEAL_1, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void gatAlienMeal() {
        service.get(START_SEQ, ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        LocalDate start = LocalDate.of(2015, Month.MAY, 30);
        LocalDate end = LocalDate.of(2015, Month.MAY, 30);
        List<Meal> meals = service.getBetweenDates(start, end, USER_ID);
        assertMatch(meals, MEAL_2, MEAL_1);
    }

    @Test
    public void getBetweenDateTimes() {
        LocalDateTime start = LocalDateTime.of(2015, Month.MAY, 30, 7, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, Month.MAY, 30, 8, 0, 0);
        List<Meal> meals = service.getBetweenDateTimes(start, end, USER_ID);
        assertMatch(meals, Collections.EMPTY_LIST);
    }
}