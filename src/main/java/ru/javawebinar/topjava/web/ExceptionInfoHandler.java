package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.util.exception.ErrorType.*;
import static ru.javawebinar.topjava.web.ErrorMessageUtil.MEAL_DUPLICATED_DATE_TIME;
import static ru.javawebinar.topjava.web.ErrorMessageUtil.USER_DUPLICATED_EMAIL;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    @Autowired
    private ErrorMessageUtil errorMessageUtil;

    private Map<String, String> ERRORS_MAP = Map.of(
            "users_unique_email_idx", USER_DUPLICATED_EMAIL,
            "meals_unique_user_datetime_idx", MEAL_DUPLICATED_DATE_TIME);

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler({DataIntegrityViolationException.class, IllegalRequestDataException.class})
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String errorMessage = ValidationUtil.getRootCause(e).getMessage();
        List<String> messageList = new ArrayList<>();
        for (Map.Entry<String, String> entry : ERRORS_MAP.entrySet()) {
            if (errorMessage.contains(entry.getKey())) {
                messageList.add(errorMessage);
                messageList.add(errorMessageUtil.getMessage(entry.getValue()));
            }
        }
        return getError(req, DATA_ERROR, messageList);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        List<String> messageList = new ArrayList<>();
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }

        if (rootCause instanceof MethodArgumentNotValidException) {
            messageList = getMessages(((MethodArgumentNotValidException) rootCause).getBindingResult().getFieldErrors());
        } else if (rootCause instanceof BindException) {
            messageList = getMessages(((BindException) rootCause).getFieldErrors());
        } else if (rootCause instanceof NotFoundException) {
            messageList.add(rootCause.getMessage());
        } else {
            messageList.add(rootCause.toString());
        }

        return getError(req, errorType, messageList);
    }

    private List<String> getMessages(List<FieldError> errorList) {
        List<String> list = new ArrayList<>();
        errorList.forEach(fieldError -> list.add(fieldError.getField()));
        return list;
    }

    private ErrorInfo getError(HttpServletRequest req, ErrorType errorType, List<String> messageList) {
        return new ErrorInfo(req.getRequestURL(), errorType, messageList);
    }
}