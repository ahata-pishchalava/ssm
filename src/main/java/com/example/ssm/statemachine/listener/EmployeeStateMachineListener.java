package com.example.ssm.statemachine.listener;

import com.example.ssm.model.EmployeeEvent;
import com.example.ssm.model.EmployeeState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class EmployeeStateMachineListener extends StateMachineListenerAdapter<EmployeeState, EmployeeEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeStateMachineListener.class);

    @Override
    public void stateChanged(final State<EmployeeState, EmployeeEvent> from, final State<EmployeeState, EmployeeEvent> to) {
        LOGGER.info(String.format("Transitioned from state %s to state %s%n", from == null ?
                "none" : from.getId(), to.getId()));
    }

    @Override
    public void eventNotAccepted(final Message<EmployeeEvent> event) {
        LOGGER.error(String.format("Event %s is not accepted.", event.toString()));
    }

    @Override
    public void stateMachineStarted(final StateMachine<EmployeeState, EmployeeEvent> stateMachine) {
        LOGGER.error(String.format("Machine %s is started.", stateMachine.getId()));
    }
}