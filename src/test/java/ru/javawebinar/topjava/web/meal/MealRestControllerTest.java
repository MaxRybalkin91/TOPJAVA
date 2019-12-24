package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.TestUtil.readFromJsonMvcResult;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.util.MealsUtil.createTo;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MealService mealService;

    public MealRestControllerTest() {
        super(MealRestController.REST_URL);
    }

    @Test
    public void get() throws Exception {
        perform(doGet(ADMIN_MEAL_ID).basicAuth(ADMIN))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> MEAL_MATCHERS.assertMatch(readFromJsonMvcResult(result, Meal.class), ADMIN_MEAL1));
    }

    @Test
    public void getUnauth() throws Exception {
        perform(doGet(MEAL1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getNotFound() throws Exception {
        perform(doGet(ADMIN_MEAL_ID).basicAuth(USER))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void delete() throws Exception {
        perform(doDelete(MEAL1_ID).basicAuth(USER))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        perform(doDelete(ADMIN_MEAL_ID).basicAuth(USER))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void update() throws Exception {
        perform(doPut(MEAL1_ID).jsonBody(MealTestData.getUpdated()).basicAuth(USER))
                .andExpect(status().isNoContent());

        MEAL_MATCHERS.assertMatch(mealService.get(MEAL1_ID, START_SEQ), MealTestData.getUpdated());
    }

    @Test
    public void createWithLocation() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = perform(doPost().jsonBody(newMeal).basicAuth(USER));

        Meal created = readFromJson(action, Meal.class);
        Integer newId = created.getId();
        newMeal.setId(newId);
        MEAL_MATCHERS.assertMatch(created, newMeal);
        MEAL_MATCHERS.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void getAll() throws Exception {
        perform(doGet().basicAuth(USER))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHERS.contentJson(getTos(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    public void filter() throws Exception {
        perform(doGet("filter").basicAuth(USER).unwrap()
                .param("startDate", "2015-05-30").param("startTime", "07:00")
                .param("endDate", "2015-05-31").param("endTime", "11:00"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MEAL_TO_MATCHERS.contentJson(createTo(MEAL5, true), createTo(MEAL1, false)));
    }

    @Test
    public void filterAll() throws Exception {
        perform(doGet("filter?startDate=&endTime=").basicAuth(USER))
                .andExpect(status().isOk())
                .andExpect(MEAL_TO_MATCHERS.contentJson(getTos(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    public void createUnprocessableEntity() throws Exception {
        perform(doPost().jsonBody(MealTestData.getInvalid()).basicAuth(USER))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updateUnprocessableEntity() throws Exception {
        perform(doPut(MEAL1_ID).jsonBody(MealTestData.getInvalid()).basicAuth(USER))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createWithDuplication() throws Exception {
        perform(doPost().jsonBody(MealTestData.getDuplicated()).basicAuth(USER));
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void updateWithDuplication() throws Exception {
        Meal updated = new Meal(MEAL2);
        updated.setDateTime(MEAL1.getDateTime());
        perform(doPut(MEAL2.getId()).jsonBody(updated).basicAuth(USER));
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }
}