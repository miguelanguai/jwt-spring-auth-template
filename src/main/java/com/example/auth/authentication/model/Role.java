package com.example.auth.authentication.model;

import com.example.auth.user.model.UserEntity;

/**
 * The Role the {@link UserEntity} has. This allows him to call the different
 * auth-protected endpoints the API has
 */
public enum Role {
    USER, ADMIN
}
