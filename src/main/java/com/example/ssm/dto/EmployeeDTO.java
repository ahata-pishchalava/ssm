package com.example.ssm.dto;

import com.example.ssm.model.EmployeeState;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EmployeeDTO {
    private UUID id;

    private String name;

    private String email;

    private EmployeeState state;

}
