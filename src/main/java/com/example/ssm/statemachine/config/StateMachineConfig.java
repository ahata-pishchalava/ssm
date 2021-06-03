package com.example.ssm.statemachine.config;

import com.example.ssm.model.EmployeeEvent;
import com.example.ssm.model.EmployeeState;
import com.example.ssm.statemachine.action.ActivateAction;
import com.example.ssm.statemachine.action.ApproveAction;
import com.example.ssm.statemachine.action.CheckAction;
import com.example.ssm.statemachine.action.ErrorAction;
import com.example.ssm.statemachine.listener.EmployeeStateMachineListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory(name = "employeeStateMachineFactory")
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<EmployeeState, EmployeeEvent> {

    @Override
    public void configure(final StateMachineConfigurationConfigurer<EmployeeState, EmployeeEvent> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(final StateMachineStateConfigurer<EmployeeState, EmployeeEvent> states) throws Exception {
        states
                .withStates()
                .initial(EmployeeState.ADDED)
                .end(EmployeeState.ACTIVE)
                .states(EnumSet.allOf(EmployeeState.class));

    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<EmployeeState, EmployeeEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(EmployeeState.ADDED)
                .target(EmployeeState.IN_CHECK)
                .event(EmployeeEvent.CHECK)
                .action(checkAction(), errorAction())

                .and()
                .withExternal()
                .source(EmployeeState.IN_CHECK)
                .target(EmployeeState.APPROVED)
                .event(EmployeeEvent.APPROVE)
                .action(activateAction(), errorAction())

                .and()
                .withExternal()
                .source(EmployeeState.APPROVED)
                .target(EmployeeState.ACTIVE)
                .event(EmployeeEvent.ACTIVATE)
                .action(activateAction(), errorAction());
    }

    @Bean
    public Action<EmployeeState, EmployeeEvent> checkAction() {
        return new CheckAction();
    }

    @Bean
    public Action<EmployeeState, EmployeeEvent> activateAction() {
        return new ActivateAction();
    }

    @Bean
    public Action<EmployeeState, EmployeeEvent> approveAction() {
        return new ApproveAction();
    }

    @Bean
    public Action<EmployeeState, EmployeeEvent> errorAction() {
        return new ErrorAction();
    }

    @Bean
    public StateMachineListener<EmployeeState, EmployeeEvent> listener() {
        return new EmployeeStateMachineListener();
    }
}
