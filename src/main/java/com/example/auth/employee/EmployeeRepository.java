package com.example.auth.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auth.employee.model.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

}
