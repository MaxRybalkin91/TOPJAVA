package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.storage.MapStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MapStorage storage = new MapStorage();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String userCalories = request.getParameter("dailyCalories");
        if (!isEmpty(userCalories)) {
            User.setCaloriesPerDay(Integer.parseInt(userCalories));
            response.sendRedirect("meals");
            return;
        }

        String mealId = request.getParameter("id");
        boolean isMealIdEmpty = isEmpty(mealId);
        log.info(isMealIdEmpty ? "adding" : "editing");

        Meal meal = new Meal();
        meal.setDateTime(LocalDateTime.parse(request.getParameter("localDateTime")));
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        if (isMealIdEmpty) {
            storage.save(meal);
        } else {
            storage.update(Integer.parseInt(mealId), meal);
        }
        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("redirect to meals");
        String page = "meals.jsp";
        String action = request.getParameter("action");

        if (!isEmpty(action)) {
            switch (action) {
                case "delete":
                    log.info("deleting");
                    storage.delete(getId(request));
                    break;
                case "add":
                case "edit":
                    log.info(action.equals("add") ? "adding" : "editing");
                    Meal meal = action.equals("add") ? new Meal(LocalDateTime.now(), "", 0) : storage.get(getId(request));
                    request.setAttribute("meal", meal);
                    page = "edit.jsp";
                    break;
                case "fill":
                    storage.fillMapStorage();
                    break;
            }
        }
        request.setAttribute("meals", MealsUtil.convertWithExcess(storage.getAll(), LocalTime.MIN, LocalTime.MAX));
        request.getRequestDispatcher(page).forward(request, response);
    }

    private boolean isEmpty(String... values) {
        boolean isEmpty = false;
        for (String value : values) {
            if (value == null || value.trim().length() == 0) {
                isEmpty = true;
                break;
            }
        }
        return isEmpty;
    }

    private int getId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}