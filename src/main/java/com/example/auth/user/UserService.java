package com.example.auth.user;

import com.example.auth.user.model.UserDto;
import com.example.auth.user.model.UserEntity;

public interface UserService {

    /**
     * returns current user email
     * 
     * @return the {@link UserEntity}'s mail
     */
    String getUserEmail();

    /**
     * returns current user
     * 
     * @param username
     * @return current {@link UserEntity}
     */
    UserEntity getCurrentUser();

    /**
     * updates a user
     * 
     * @param id
     * @param userDto
     */
    void save(Long id, UserDto userDto);

    /**
     * checks if {@link UserEntity} passed in the parameter is admin
     * 
     * @param {@link UserEntity}
     * @return boolean
     */
    boolean isAdmin(UserEntity user);
}
