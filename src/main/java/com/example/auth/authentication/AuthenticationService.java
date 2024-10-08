package com.example.auth.authentication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth.authentication.model.Token;
import com.example.auth.user.UserRepository;
import com.example.auth.user.UserService;
import com.example.auth.user.model.UserEntity;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
            TokenRepository tokenRepository, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a {@link UserEntity} on the web. <br>
     * <br>
     * It verifies his existance on the database, passes all the attributes and the
     * {@link Role},saves it on the database, generates a {@link Token} and calls
     * {@link #saveUserToken(String, UserEntity) saveUserToken} to save the
     * {@link UserEntity} token
     * 
     * @param request
     * @return {@link AuthenticationResponse}
     */
    public AuthenticationResponse register(UserEntity request) {

        if (repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "User already exist");
        }

        UserEntity user = new UserEntity();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMail(request.getMail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        user = repository.save(user);

        String jwt = jwtService.generateToken(user);

        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User registration was successful");

    }

    /**
     * Logins on the application with a {@link UserEntity} username and password.
     * <br>
     * <br>
     * {@link AuthenticationManager} authenticates if username and password are
     * correct. Finds the {@link UserEntity} in the database and generates a
     * {@link Token} with it. All previous {@link Token}s associated to the user are
     * revoked and the new one created is saved
     * 
     * @param request
     * @return {@link AuthenticationResponse}
     */
    public AuthenticationResponse authenticate(UserEntity request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserEntity user = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User login was successful");

    }

    /**
     * Revokes all the {@link Token}s a {@link UserEntity} has and saves these
     * changes on the database.
     * 
     * @param user
     */
    public void revokeAllTokenByUser(UserEntity user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if (validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t -> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }

    /**
     * Saves a {@link UserEntity}'s {@link Token} on the database. <br>
     * <br>
     * Creates a new {@link Token} object, sets its jwt, puts loggedOut to false and
     * sets the {@link UserEntity} that owns the {@link Token}
     * 
     * @param jwt
     * @param user
     */
    public void saveUserToken(String jwt, UserEntity user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    /**
     * Signs out of a session. <br>
     * <br>
     * Gets the {@link UserEntity} closing his session, and calls the method
     * {@link #revokeAllTokenByUser(UserEntity) revokeAllTokenByUser} to delete the
     * {@link Token}
     * 
     * @param signoutRequest
     * @return {@link AuthenticationResponse}
     */
    public AuthenticationResponse signOut(UserEntity signoutRequest) {
        AuthenticationResponse response;
        UserEntity userLeaving = this.userService.getCurrentUser();

        try {
            this.revokeAllTokenByUser(userLeaving);
            response = new AuthenticationResponse(null, "Successfully Signed Out");
        } catch (Exception e) {
            response = new AuthenticationResponse(null, e.getMessage());
        }
        return response;
    }
}
