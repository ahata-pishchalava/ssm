package com.example.ssm.repository;

import com.example.ssm.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    @Query("SELECT empl FROM Employee empl WHERE empl.name = ?1")
    Optional<Employee> findByName(String name);
}
