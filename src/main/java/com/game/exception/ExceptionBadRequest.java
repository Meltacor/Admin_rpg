package com.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceptionBadRequest extends RuntimeException{
    public ExceptionBadRequest() {
        super("Data is not correct");
    }
}
