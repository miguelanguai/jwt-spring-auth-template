package com.example.auth.employee;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.employee.model.EmployeeDto;
import com.example.auth.employee.model.EmployeeEntity;

import io.swagger.v3.oas.annotations.Operation;

@RequestMapping(value = "")
@RestController
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    ModelMapper mapper;

    @Autowired
    EmployeeService entityService;

    /**
     * Retrieves all the existing {@link EmployeeEntity} as {@link EmployeeDto}
     * 
     * @return
     */
    @Operation(summary = "Find All", description = "Method to retrieve all employees")
    @RequestMapping(path = "/p/employee", method = RequestMethod.GET)
    public List<EmployeeDto> findAll() {
        List<EmployeeEntity> entities = this.entityService.findAll();
        return entities.stream().map(e -> mapper.map(e, EmployeeDto.class)).collect(Collectors.toList());
    }

    /**
     * Creates a {@link EmployeeEntity} passing a {@link EmployeeDto} in the body or
     * updates an existing one also passing his id as a parameter
     * 
     * @param id
     * @param employeeDto
     * @throws Exception
     */
    @Operation(summary = "Create or Update", description = "Method that creates or updates an Employee")
    @RequestMapping(path = { "/u/employee", "/u/employee/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody EmployeeDto employeeDto)
            throws Exception {
        this.entityService.save(id, employeeDto);
    }
}
