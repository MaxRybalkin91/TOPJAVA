package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessageUtil {

    public static final String USER_DUPLICATED_EMAIL = "user.duplicateEmail";
    public static final String MEAL_DUPLICATED_DATE_TIME = "meal.duplicateDateTime";
    @Autowired
    private MessageSource messageSource;

    public String getMessage(String message) {
        return messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
    }
}