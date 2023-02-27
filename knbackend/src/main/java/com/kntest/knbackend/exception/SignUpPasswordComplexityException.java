package com.kntest.knbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SignUpPasswordComplexityException extends Exception {
    public SignUpPasswordComplexityException(String errorMessage) {
        super(errorMessage);
    }
}
