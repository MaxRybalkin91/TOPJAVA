package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MapStorage;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MapStorage storage = new MapStorage();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String meal_id = request.getParameter("id");
        Meal meal;

        if (isEmpty(meal_id)) {
            meal = new Meal();
        } else {
            int id = Integer.parseInt(meal_id);
            LocalDateTime ldt = TimeUtil.toDateTime(request.getParameter("localDateTime"));
            String description = request.getParameter("description");
            int calories = Integer.parseInt(request.getParameter("calories"));
            meal = new Meal(id, ldt, description, calories);
        }
        storage.save(meal);
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
                    storage.delete(getId(request));
                    break;
                case "add":
                case "edit":
                    Meal meal = action.equals("add") ? new Meal() : storage.get(getId(request));
                    request.setAttribute("meal", meal);
                    page = "edit.jsp";
                    break;
                case "fill":
                    storage.fillMapStorage();
                    break;
                case "setCalories":
                    int calories = Integer.parseInt(request.getParameter("calories"));
                    MealTo.setCaloriesPerDay(calories);
                    break;
            }
        }
        request.setAttribute("meals", MealsUtil.convertWithExcess(storage.getAll()));
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