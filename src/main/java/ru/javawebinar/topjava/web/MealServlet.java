package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MapStorage;
import ru.javawebinar.topjava.storage.Storage;
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
    private Storage storage = new MapStorage();

    @Override
    public void init() {
        storage.fillStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String mealId = request.getParameter("id");
        boolean isMealIdEmpty = isEmpty(mealId);
        log.info(isMealIdEmpty ? "adding" : "editing");

        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("localDateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(dateTime, description, calories);
        if (!isMealIdEmpty) {
            meal.setId(Integer.parseInt(mealId));
        }
        storage.save(meal);
        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("redirect to meals");
        String action = request.getParameter("action");

        if (!isEmpty(action)) {
            switch (action) {
                case "delete":
                    log.info("deleting");
                    storage.delete(getId(request));
                    response.sendRedirect("meals");
                    return;
                case "add":
                case "edit":
                    log.info(action.equals("add") ? "adding" : "editing");
                    Meal meal = action.equals("add") ? new Meal(LocalDateTime.now(), "", 0) : storage.get(getId(request));
                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("/jsp/meal/edit.jsp").forward(request, response);
                    return;
                default:
                    break;
            }
        }
        request.setAttribute("meals", MealsUtil.convertWithExcess(storage.getAll(), LocalTime.MIN, LocalTime.MAX));
        request.getRequestDispatcher("/jsp/meal/meals.jsp").forward(request, response);
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    private int getId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}