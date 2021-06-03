package com.example.ssm.statemachine.handler;

import com.example.ssm.model.EmployeeEvent;
import com.example.ssm.model.EmployeeState;
import com.example.ssm.statemachine.interceptor.EmployeePersistStateChangeInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmployeePersistStateHandler {

    @Autowired
    private final EmployeePersistStateChangeInterceptor employeePersistStateChangeInterceptor;

    public boolean handleEventWithState(Message<EmployeeEvent> event,
                                        EmployeeState sourceState,
                                        StateMachine<EmployeeState, EmployeeEvent> stateMachine) {
        stateMachine.stop();
        stateMachine
                .getStateMachineAccessor()
                .doWithAllRegions(
                        function -> {
                            function.addStateMachineInterceptor(employeePersistStateChangeInterceptor);
                            function.resetStateMachine(
                                    new DefaultStateMachineContext<>(sourceState, null, null, null, null, stateMachine.getId()));

                        });
        stateMachine.start();
        return stateMachine.sendEvent(event);
    }
}