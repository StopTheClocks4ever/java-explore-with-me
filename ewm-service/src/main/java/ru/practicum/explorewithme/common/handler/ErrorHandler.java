package ru.practicum.explorewithme.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler({CategoryNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final RuntimeException e) {
        return new ApiError(HttpStatus.NOT_FOUND.toString(), e.getClass().toString(), e.getLocalizedMessage(), LocalDateTime.now().format(FORMATTER));
    }
}
