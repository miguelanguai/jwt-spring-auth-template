package com.example.auth.user.model;

import java.util.List;

import com.example.auth.authentication.model.Role;
import com.example.auth.authentication.model.Token;

public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String mail;

    private String username;

    private String password;

    private Role role;

    private List<Token> tokens;

    /**
     * 
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id new value of {@link #getId}
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * 
     * @param firstName new value of {@link #getFirstName}
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * 
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * 
     * @param lastName new value of {@link #getLastName}
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * 
     * @return mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * 
     * @param mail new value of {@link #getMail}
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * 
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @param username new value of {@link #getUsername}
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password new value of {@link #getPassword}
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 
     * @return {@link Role}
     */
    public Role getRole() {
        return role;
    }

    /**
     * 
     * @param role new value of {@link #getRole}
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * 
     * @return {@link List} of {@link Token}s
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * 
     * @param tokens new value of {@link #getTokens}
     */
    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

}
