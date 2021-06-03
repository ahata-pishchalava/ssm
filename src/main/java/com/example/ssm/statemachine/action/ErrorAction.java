package com.example.ssm.statemachine.action;

import com.example.ssm.model.EmployeeEvent;
import com.example.ssm.model.EmployeeState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class ErrorAction implements Action<EmployeeState, EmployeeEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorAction.class);

    @Override
    public void execute(final StateContext<EmployeeState, EmployeeEvent> context) {
        Exception exception = context.getException();
        LOGGER.error("Error Action was called. The problem is: {}", exception.getMessage());
    }
}
