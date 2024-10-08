package com.example.auth.employee;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.auth.employee.model.EmployeeDto;
import com.example.auth.employee.model.EmployeeEntity;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmployeeEntity> findAll() {
        return (List<EmployeeEntity>) this.employeeRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, EmployeeDto employeeDto) throws Exception {
        if (employeeDto == null)
            throw new Exception("Employee cannot be null");

        EmployeeEntity employee;
        if (id == null) {
            employee = new EmployeeEntity();
        } else {
            employee = this.employeeRepository.findById(id).orElse(null);
        }
        BeanUtils.copyProperties(employeeDto, employee);
        employee.setId(id);

        this.employeeRepository.save(employee);
    }

}
