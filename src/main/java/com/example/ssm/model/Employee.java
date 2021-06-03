package com.example.ssm.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee {
    public static final String EMPLOYEE_ID_KEY = "employeeId";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private EmployeeState state = EmployeeState.ADDED;
}
