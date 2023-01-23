package ru.skypro.homework.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdsNotFoundException extends RuntimeException{
    public AdsNotFoundException() {
        super();
    }

    public AdsNotFoundException(String message) {
        super(message);
    }

    public AdsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdsNotFoundException(Throwable cause) {
        super(cause);
    }

    protected AdsNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
