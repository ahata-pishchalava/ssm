package com.example.ssm.statemachine.action;

import com.example.ssm.model.Employee;
import com.example.ssm.model.EmployeeEvent;
import com.example.ssm.model.EmployeeState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.UUID;

public class CheckAction implements Action<EmployeeState, EmployeeEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckAction.class);

    @Override
    public void execute(final StateContext<EmployeeState, EmployeeEvent> context) {
        LOGGER.info("Checking Action was called for Employee with id: {}", context.getMessageHeaders().get(Employee.EMPLOYEE_ID_KEY, UUID.class));
    }
}