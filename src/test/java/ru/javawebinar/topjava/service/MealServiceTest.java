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
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

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
    private static final List<Meal> mealOfUserList = Arrays.asList(MEAL_USER_DINNER_2, MEAL_USER_LUNCH_2, MEAL_USER_BREAKFAST_2,
            MEAL_USER_DINNER_1, MEAL_USER_LUNCH_1, MEAL_USER_BREAKFAST_1);

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(NEW_USER_DINNER);
        Meal created = service.create(newMeal, USER_ID);
        int mealId = created.getId();
        newMeal.setId(mealId);
        assertMatch(service.get(mealId, USER_ID), newMeal);
    }

    @Test
    public void get() {
        Meal meal1 = service.get(MEAL_USER_LUNCH_1.getId(), USER_ID);
        assertMatch(meal1, MEAL_USER_LUNCH_1);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), mealOfUserList);
    }

    @Test
    public void delete() {
        List<Meal> deletedUserMealList = new ArrayList<>(mealOfUserList);
        deletedUserMealList.remove(MEAL_USER_BREAKFAST_1);
        service.delete(MEAL_USER_BREAKFAST_1.getId(), USER_ID);
        assertMatch(service.getAll(USER_ID), deletedUserMealList);
    }

    @Test
    public void update() {
        Meal meal1 = new Meal(MEAL_ADMIN_LUNCH_2);
        Meal meal2 = new Meal(MEAL_ADMIN_LUNCH_2);
        meal1.setDescription("I haven't eaten");
        meal1.setCalories(0);
        meal2.setDescription("I haven't eaten");
        meal2.setCalories(0);
        service.update(meal1, ADMIN_ID);
        assertMatch(service.get(meal1.getId(), ADMIN_ID), meal2);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateCreate() throws Exception {
        Meal meal = new Meal(MEAL_ADMIN_BREAKFAST_2);
        meal.setId(null);
        service.create(meal, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAlienMeal() {
        service.delete(MEAL_USER_DINNER_2.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateAlienMeal() {
        service.update(MEAL_ADMIN_BREAKFAST_1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void gatAlienMeal() {
        service.get(MEAL_ADMIN_DINNER_2.getId(), USER_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> mealOfAdminList = Arrays.asList(MEAL_ADMIN_DINNER_1, MEAL_ADMIN_LUNCH_1, MEAL_ADMIN_BREAKFAST_1);
        LocalDate start = LocalDate.of(2015, Month.MAY, 30);
        LocalDate end = LocalDate.of(2015, Month.MAY, 30);
        List<Meal> meals = service.getBetweenDates(start, end, ADMIN_ID);
        assertMatch(meals, mealOfAdminList);
    }
}