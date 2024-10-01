package com.simeon.webservices.licenta_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateIngredientException extends RuntimeException{
    public DuplicateIngredientException(String message) {
        super(message);
    }
}
