package com.example.ssm.mapper;

import com.example.ssm.dto.EmployeeDTO;
import com.example.ssm.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeDTO entityToEntityDTO(Employee entity);
    Employee entityDTOToEntity(EmployeeDTO entityDTO);
}