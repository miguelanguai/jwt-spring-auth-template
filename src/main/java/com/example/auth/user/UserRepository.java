package com.example.auth.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auth.user.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * finds a {@link UserEntity} by passing his username
     * 
     * @param username
     * @return {@link UserEntity}
     */
    Optional<UserEntity> findByUsername(String username);
}
