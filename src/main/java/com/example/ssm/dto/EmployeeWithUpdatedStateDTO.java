package com.example.ssm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EmployeeWithUpdatedStateDTO extends EmployeeDTO {
    private boolean isStateChanged = false;
}
