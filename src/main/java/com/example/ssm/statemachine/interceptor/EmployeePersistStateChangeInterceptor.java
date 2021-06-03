package com.example.ssm.statemachine.interceptor;

import com.example.ssm.exception.EntityNotFoundException;
import com.example.ssm.model.Employee;
import com.example.ssm.model.EmployeeEvent;
import com.example.ssm.model.EmployeeState;
import com.example.ssm.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class EmployeePersistStateChangeInterceptor extends StateMachineInterceptorAdapter<EmployeeState, EmployeeEvent> {
    private final static Logger LOGGER = LoggerFactory.getLogger(EmployeePersistStateChangeInterceptor.class);

    private final EmployeeRepository employeeRepository;

    @SneakyThrows
    @Override
    public void preStateChange(State<EmployeeState, EmployeeEvent> state,
                               Message<EmployeeEvent> message,
                               Transition<EmployeeState, EmployeeEvent> transition,
                               StateMachine<EmployeeState, EmployeeEvent> stateMachine) {
        if (message != null && message.getHeaders().containsKey(Employee.EMPLOYEE_ID_KEY)) {
            UUID id = message.getHeaders().get(Employee.EMPLOYEE_ID_KEY, UUID.class);
            if (id != null) {
                Employee entity = employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                        String.format("There is no entity of type %s with id %s in DB!", Employee.class, id)));
                entity.setState(state.getId());
                LOGGER.info("Persist change of the employee state. Employee: {}", entity.toString());
                employeeRepository.save(entity);
            }
        }
    }
}