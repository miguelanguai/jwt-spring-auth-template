package com.example.auth.employee;

import java.util.List;

import com.example.auth.employee.model.EmployeeDto;
import com.example.auth.employee.model.EmployeeEntity;

public interface EmployeeService {

    /**
     * Retrieves all the existing employees and returns them on a {@link List} of
     * {@link EmployeeEntity}
     * 
     * @return
     */
    List<EmployeeEntity> findAll();

    /**
     * Creates a {@link EmployeeEntity} passing a {@link EmployeeDto} in the body or
     * updates an existing one also passing his id as a parameter
     * 
     * @param id
     * @param employeeDto
     * @throws Exception
     */
    void save(Long id, EmployeeDto employeeDto) throws Exception;

}
