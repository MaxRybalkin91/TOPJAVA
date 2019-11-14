package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends AbstractMealController {

    @GetMapping("/meals")
    public String getMeals(Model model) {
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @GetMapping("/meals/filter")
    public String getFilteredMeals(
            Model model,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String startTime,
            @RequestParam String endTime
    ) {
        LocalDate parsedStartDate = parseLocalDate(startDate);
        LocalDate parsedEndDate = parseLocalDate(endDate);
        LocalTime parsedStartTime = parseLocalTime(startTime);
        LocalTime parsedEndTime = parseLocalTime(endTime);

        List<MealTo> filteredMeals = getBetween(parsedStartDate, parsedStartTime, parsedEndDate, parsedEndTime);
        model.addAttribute("meals", filteredMeals);
        return "meals";
    }

    @GetMapping("/meals/add")
    public String addMeal(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/meals/update")
    public String editMeal(
            Model model,
            @RequestParam String id) {
        Meal meal = get(Integer.parseInt(id));
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/meals/save")
    public String saveMeal(
            @RequestParam String id,
            @RequestParam String dateTime,
            @RequestParam String description,
            @RequestParam String calories) {
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
        if (!StringUtils.isEmpty(id)) {
            update(meal, Integer.parseInt(id));
        } else {
            create(meal);
        }
        return "redirect:/meals";
    }

    @GetMapping("/meals/delete")
    public String deleteMeal(
            @RequestParam String id) {
        delete(Integer.parseInt(id));
        return "meals";
    }
}
