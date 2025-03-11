package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateUsername(DuplicateUsernameException e) {
        return e.getMessage();
    }

    @ExceptionHandler(AccountRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUnsuccessfulAccountRegistration(AccountRegistrationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnsuccessfulLogin(AuthenticationException e) {
        return e.getMessage();
    }
}
