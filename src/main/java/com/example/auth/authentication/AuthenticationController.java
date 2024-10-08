package com.example.auth.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.user.model.UserEntity;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * Registers a {@link UserEntity} on the application
     * 
     * @param request
     * @return {@link AuthenticationResponse} and ok body.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserEntity request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Logins on the application with a {@link UserEntity} username and password
     * 
     * @param request
     * @return {@link AuthenticationResponse} and ok body.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserEntity request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    /**
     * Makes the token no longer work for the {@link UserEntity}
     * 
     * @param signOutRequest
     * @return {@link AuthenticationResponse} to null and ok body.
     */
    @PostMapping("/user/signout")
    public ResponseEntity<AuthenticationResponse> signOut(@RequestBody UserEntity signOutRequest) throws Exception {
        return ResponseEntity.ok(authService.signOut(signOutRequest));
    }
}
