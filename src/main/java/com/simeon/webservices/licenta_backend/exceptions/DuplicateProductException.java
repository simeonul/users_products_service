package com.simeon.webservices.licenta_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateProductException extends RuntimeException{
    public DuplicateProductException(String message) {
        super(message);
    }
}
