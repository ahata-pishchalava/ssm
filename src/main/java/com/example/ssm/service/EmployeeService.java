package com.example.ssm.service;

import com.example.ssm.dto.EmployeeDTO;
import com.example.ssm.exception.EntityNotFoundException;
import com.example.ssm.mapper.EmployeeMapper;
import com.example.ssm.model.Employee;
import com.example.ssm.model.EmployeeEvent;
import com.example.ssm.model.EmployeeState;
import com.example.ssm.repository.EmployeeRepository;
import com.example.ssm.statemachine.handler.EmployeePersistStateHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmployeeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private final EmployeePersistStateHandler employeePersistStateHandler;

    @Autowired
    private final StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;

    @Transactional
    public EmployeeDTO addEmployee(Employee employee) {
        LOGGER.info("Adding an employee {} to system.", employee);
        employee.setState(EmployeeState.ADDED);
        return employeeMapper.entityToEntityDTO(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeDTO checkEmployee(UUID employeeId) throws EntityNotFoundException {
        LOGGER.info("Checking the employee with Id {}.", employeeId);
        boolean isUpdated = updateState(employeeId, EmployeeEvent.CHECK);
        EmployeeDTO employeeDTO = employeeMapper.entityToEntityDTO(employeeRepository.getById(employeeId));
        employeeDTO.setStateChanged(isUpdated);
        return employeeDTO;
    }

    @Transactional
    public EmployeeDTO approveEmployee(UUID employeeId) throws EntityNotFoundException {
        LOGGER.info("Approving the employee with Id {}.", employeeId);
        boolean isUpdated = updateState(employeeId, EmployeeEvent.APPROVE);
        EmployeeDTO employeeDTO = employeeMapper.entityToEntityDTO(employeeRepository.getById(employeeId));
        employeeDTO.setStateChanged(isUpdated);
        return employeeDTO;
    }

    @Transactional
    public EmployeeDTO activateEmployee(UUID employeeId) throws EntityNotFoundException {
        LOGGER.info("Activating the employee with Id {}.", employeeId);
        boolean isUpdated = updateState(employeeId, EmployeeEvent.ACTIVATE);
        EmployeeDTO employeeDTO = employeeMapper.entityToEntityDTO(employeeRepository.getById(employeeId));
        employeeDTO.setStateChanged(isUpdated);
        return employeeDTO;
    }

    @SneakyThrows
    private Boolean updateState(UUID id, EmployeeEvent event) {
        Employee entity = employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("There is no entity of type %s with id %s in DB!", Employee.class, id)));

        Message<EmployeeEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(Employee.EMPLOYEE_ID_KEY, entity.getId())
                .build();


        return employeePersistStateHandler.handleEventWithState(message, entity.getState(),
                stateMachineFactory.getStateMachine(entity.getId().toString()));
    }
}
