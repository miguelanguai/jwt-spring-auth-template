package com.example.auth.authentication;

/**
 * This class represents the response sent after successful authentication. It
 * contains the {@link Token} generated for the user and a message.
 */
public class AuthenticationResponse {
    private String token;
    private String message;

    public AuthenticationResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    /**
     * 
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * 
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
