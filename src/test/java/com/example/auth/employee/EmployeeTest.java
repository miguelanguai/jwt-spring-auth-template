package com.example.auth.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.auth.employee.model.EmployeeDto;
import com.example.auth.employee.model.EmployeeEntity;

@ExtendWith(MockitoExtension.class)
public class EmployeeTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    public static final String EMPLOYEE_NAME = "ENT1";
    public static final Long EXISTS_EMPLOYEE_ID = 1L;

    @Test
    public void findAllShouldReturnAllEmployees() {

        List<EmployeeEntity> list = new ArrayList<>();
        list.add(mock(EmployeeEntity.class));

        when(employeeRepository.findAll()).thenReturn(list);

        List<EmployeeEntity> employees = employeeRepository.findAll();

        assertNotNull(employees);
        assertEquals(1, employees.size());
    }

    @Test
    public void saveNotExistsEmployeeIdShouldInsert() throws Exception {

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName(EMPLOYEE_NAME);

        ArgumentCaptor<EmployeeEntity> employee = ArgumentCaptor.forClass(EmployeeEntity.class);

        employeeService.save(null, employeeDto);

        verify(employeeRepository).save(employee.capture());

        assertEquals(EMPLOYEE_NAME, employee.getValue().getName());
    }

    @Test
    public void saveWithoutEmployeeShouldThrowException() {
        Exception exception = assertThrows(Exception.class, () -> {
            employeeService.save(null, null);
        });

        String expectedMessage = "Employee cannot be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(employeeRepository, never()).save(any(EmployeeEntity.class));
    }

    @Test
    public void saveExistsEmployeeShouldEdit() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName(EMPLOYEE_NAME);

        EmployeeEntity employee = mock(EmployeeEntity.class);
        when(employeeRepository.findById(EXISTS_EMPLOYEE_ID)).thenReturn(Optional.of(employee));

        employeeService.save(EXISTS_EMPLOYEE_ID, employeeDto);

        verify(employeeRepository).save(employee);
    }

}
