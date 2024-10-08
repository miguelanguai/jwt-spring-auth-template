package com.example.auth.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.auth.authentication.model.Role;
import com.example.auth.authentication.model.Token;
import com.example.auth.user.UserRepository;
import com.example.auth.user.UserService;
import com.example.auth.user.model.UserEntity;

@ExtendWith(MockitoExtension.class)
public class AuthenticationTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setUsername("johndoe");
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMail("johndoe@mail.com");
        user.setRole(Role.USER);
    }

    @Test
    public void testRegisterSuccessful() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.register(user);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("User registration was successful", response.getMessage());

        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository).save(any(UserEntity.class));
        verify(jwtService).generateToken(user);
        verify(tokenRepository).save(any());
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        AuthenticationResponse response = authenticationService.register(user);

        assertNotNull(response);
        assertNull(response.getToken());
        assertEquals("User already exist", response.getMessage());

        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void testAuthenticateUserNotFound() {
        // Arrange
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(user));
    }

    @Test
    public void testRevokeAllTokenByUser() {
        // Arrange
        Token token = new Token();
        token.setLoggedOut(false);
        token.setUser(user);
        when(tokenRepository.findAllTokensByUser(user.getId())).thenReturn(Collections.singletonList(token));

        // Act
        authenticationService.revokeAllTokenByUser(user);

        // Assert
        assertTrue(token.getIsLoggedOut());
        verify(tokenRepository).saveAll(Collections.singletonList(token));
    }

    @Test
    public void testRevokeAllTokenByUserNoTokens() {
        // Arrange
        when(tokenRepository.findAllTokensByUser(user.getId())).thenReturn(Collections.emptyList());

        // Act
        authenticationService.revokeAllTokenByUser(user);

        // Assert
        verify(tokenRepository, never()).saveAll(any());
    }

    @Test
    public void testSaveUserToken() {
        // Arrange
        String jwt = "jwtToken";

        // Act
        authenticationService.saveUserToken(jwt, user);

        // Assert
        verify(tokenRepository).save(argThat(
                token -> token.getToken().equals(jwt) && !token.getIsLoggedOut() && token.getUser().equals(user)));
    }

}
