package com.example.auth.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.auth.authentication.JwtService;
import com.example.auth.employee.model.EmployeeDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmployeePublicIT {

    public static final String LOCALHOST = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private JwtService jwtService;

    ParameterizedTypeReference<List<EmployeeDto>> responseType = new ParameterizedTypeReference<List<EmployeeDto>>() {
    };

    public static final Long NEW_EMPLOYEE_ID = 4L;
    public static final String NEW_EMPLOYEE_NAME = "ENT4";

    public void addThreeEmployees() throws Exception {
        EmployeeDto employee1 = new EmployeeDto();
        employee1.setName("Employee 1");
        employeeService.save(null, employee1);

        EmployeeDto employee2 = new EmployeeDto();
        employee2.setName("Employee 2");
        employeeService.save(null, employee1);

        EmployeeDto employee3 = new EmployeeDto();
        employee3.setName("Employee 3");
        employeeService.save(null, employee1);
    }

    @Test
    public void findAllShouldReturnAllEntities() throws Exception {

        addThreeEmployees();

        String servicePath = "/p/employee";

        ResponseEntity<List<EmployeeDto>> response = restTemplate.exchange(LOCALHOST + port + servicePath,
                HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(3, response.getBody().size());
    }

}
