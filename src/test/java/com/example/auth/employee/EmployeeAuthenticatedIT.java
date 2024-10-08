package com.example.auth.employee;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.auth.authentication.JwtService;
import com.example.auth.authentication.UserDetailsServiceImpl;
import com.example.auth.authentication.model.Role;
import com.example.auth.config.CustomLogoutHandler;
import com.example.auth.config.SecurityConfig;
import com.example.auth.employee.model.EmployeeDto;
import com.example.auth.user.model.UserEntity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Import(SecurityConfig.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeAuthenticatedIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private CustomLogoutHandler customLogoutHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    String SECRET_KEY = "thisisanexampleofthetestofemployeecreationthatihavemadeinpurpose";
    byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);

    public UserEntity user = this.setUser();

    public String token = Jwts.builder().subject(user.getUsername()).issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
            .signWith(Keys.hmacShaKeyFor(keyBytes)).compact();

    public UserEntity setUser() {
        UserEntity user = new UserEntity();
        user.setUsername("johndoe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMail("johndoe@mail.com");
        user.setRole(Role.USER);
        return user;
    }

    @Test
    public void saveNotExistsEmployeeIdShouldInsert() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName("John Doe");

        doNothing().when(employeeService).save(any(), any(EmployeeDto.class));

        when(jwtService.extractUsername(token)).thenReturn("johndoe");
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
        when(jwtService.isValid(token, user)).thenReturn(true);

        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/u/employee").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token).content("{\"name\":\"John Doe\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(employeeService).save(any(), any(EmployeeDto.class));
    }

    @Test
    public void saveWithoutEmployeeShouldThrowException() throws Exception {
        doThrow(new Exception("Employee cannot be null")).when(employeeService).save(any(), isNull());

        when(jwtService.extractUsername(token)).thenReturn("johndoe");
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
        when(jwtService.isValid(token, user)).thenReturn(true);

        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/u/employee").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token).content("{}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(employeeService).save(any(), isNull());

        // debug
    }

    @Test
    public void saveExistsEmployeeShouldEdit() throws Exception {

    }

}
