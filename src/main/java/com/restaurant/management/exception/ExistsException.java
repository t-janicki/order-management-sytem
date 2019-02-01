package com.restaurant.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExistsException extends RuntimeException {

    public ExistsException(String exception) {
        super(exception);
    }
}
