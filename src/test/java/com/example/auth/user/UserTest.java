package com.example.auth.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.auth.authentication.model.Role;
import com.example.auth.user.model.UserDto;
import com.example.auth.user.model.UserEntity;

@ExtendWith(MockitoExtension.class)
public class UserTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private Authentication authentication;

    private UserEntity testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Configuramos un usuario de prueba
        testUser = new UserEntity();
        testUser.setUsername("johndoe");
        testUser.setRole(Role.USER);
    }

    @Test
    public void testGetUserEmail() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(testUser.getUsername());

        String email = userService.getUserEmail();

        assertEquals(testUser.getUsername(), email);
    }

    @Test
    public void testGetCurrentUserFound() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        UserEntity foundUser = userService.getCurrentUser();

        assertNotNull(foundUser);
        assertEquals(testUser.getUsername(), foundUser.getUsername());
    }

    @Test
    public void testGetCurrentUserNotFound() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        UserEntity foundUser = userService.getCurrentUser();

        assertNull(foundUser);
    }

    @Test
    public void testSave() {
        UserDto userDto = new UserDto();
        userDto.setUsername("johndoe");
        userDto.setRole(Role.USER);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setMail("johndoe@mail.com");

        userService.save(null, userDto);

        UserEntity savedUser = new UserEntity();
        BeanUtils.copyProperties(userDto, savedUser);

        verify(userRepository).save(argThat(userEntity -> userEntity.getUsername().equals(savedUser.getUsername())
                && userEntity.getRole().equals(savedUser.getRole())
                && userEntity.getFirstName().equals(savedUser.getFirstName())
                && userEntity.getLastName().equals(savedUser.getLastName())
                && userEntity.getMail().equals(savedUser.getMail())));
    }

    @Test
    public void testIsAdmin() {
        UserEntity adminUser = new UserEntity();
        adminUser.setRole(Role.ADMIN);

        assertTrue(userService.isAdmin(adminUser));
        assertFalse(userService.isAdmin(testUser));
    }
}
