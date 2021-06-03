package com.example.ssm.dto;

import com.example.ssm.model.EmployeeState;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EmployeeDTO {
    private UUID id;

    private String name;

    private String email;

    private EmployeeState state;

    private boolean isStateChanged = false;
}
