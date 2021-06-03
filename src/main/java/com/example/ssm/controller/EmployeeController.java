package com.example.ssm.controller;

import com.example.ssm.dto.EmployeeDTO;
import com.example.ssm.exception.EntityNotFoundException;
import com.example.ssm.model.Employee;
import com.example.ssm.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService service;

    @PostMapping("/add")
    public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody final Employee employee) {
         return new ResponseEntity<>(service.addEmployee(employee), HttpStatus.OK);
    }

    @PostMapping("/{employeeId}/activate")
    public ResponseEntity<EmployeeDTO> activateEmployee(@PathVariable final String employeeId) throws EntityNotFoundException {
        return new ResponseEntity<>(service.activateEmployee(UUID.fromString(employeeId)),HttpStatus.OK) ;
    }

    @PostMapping("/{employeeId}/check")
    public ResponseEntity<EmployeeDTO> inCheckEmployee(@PathVariable final String employeeId) throws EntityNotFoundException {
        return new ResponseEntity<>(service.checkEmployee(UUID.fromString(employeeId)),HttpStatus.OK);
    }

    @PostMapping("/{employeeId}/approve")
    public ResponseEntity<EmployeeDTO> approveEmployee(@PathVariable final String employeeId)  throws EntityNotFoundException{
        return new ResponseEntity<>(service.approveEmployee(UUID.fromString(employeeId)),HttpStatus.OK);
    }
}
