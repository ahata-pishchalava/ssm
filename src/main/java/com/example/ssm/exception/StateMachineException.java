package com.example.ssm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class StateMachineException extends RuntimeException {

    public StateMachineException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateMachineException(String message) {
        super(message);
    }
}
