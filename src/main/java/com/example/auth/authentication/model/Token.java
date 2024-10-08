package com.example.auth.authentication.model;

import com.example.auth.user.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The Token class represents an authentication token associated with a user.
 * Each token has a unique identifier, the token value itself, a session status
 * (whether the session is logged out or not), and is linked to a user entity
 * (UserEntity).
 * 
 * This class uses the following JPA annotations for data persistence:
 * <p>
 * -`@Entity`: Indicates that this class is an entity mapped to a table in the
 * database.
 * </p>
 * <p>
 * - `@Table`: Specifies the name of the table in the database (`token`).
 * </p>
 * <p>
 * - `@Id`: Marks the `id` field as the primary key.
 * </p>
 * <p>
 * - `@GeneratedValue`: Configures the automatic generation strategy for the
 * primary key.
 * </p>
 * <p>
 * - `@ManyToOne`: Defines a many-to-one relationship with the `UserEntity`.
 * </p>
 * <p>
 * - `@JsonBackReference`: Used to prevent circular references in JSON
 * serialization.
 * </p>
 */
@Entity
@Table(name = "token")
public class Token {

    /**
     * Unique identifier of the token. It is the primary key in the database,
     * automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * The value of the authentication token assigned to the user.
     */
    @Column(name = "token")
    private String token;

    /**
     * Indicates whether the user has logged out. true if the user is logged out,
     * false otherwise.
     */
    @Column(name = "is_logged_out")
    private boolean loggedOut;

    /**
     * The user associated with this token. The relationship is many-to-one, where a
     * user can have multiple tokens.
     */
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * 
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id new value of {@link #getId}.
     */
    public void setId(Integer id) {
        this.id = id;
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
     * @param token new value of {@link #getToken}.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 
     * @return isLoggedOut
     */
    public boolean getIsLoggedOut() {
        return loggedOut;
    }

    /**
     * 
     * @param isLoggedOut new value of {@link #getIsLoggedOut}.
     */
    public void setLoggedOut(boolean loggedOut) {
        this.loggedOut = loggedOut;
    }

    /**
     * 
     * @return {@link UserEntity}
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * 
     * @param {@link UserEntity} new value of {@link #getUser}.
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }
}
