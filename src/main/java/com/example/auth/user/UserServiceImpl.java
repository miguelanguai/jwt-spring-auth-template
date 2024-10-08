package com.example.auth.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.auth.authentication.model.Role;
import com.example.auth.user.model.UserDto;
import com.example.auth.user.model.UserEntity;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByUsername(authentication.getName()).orElse(null);
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, UserDto userDto) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(userDto, user);
        this.userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAdmin(UserEntity user) {
        return user.getRole() == Role.ADMIN;
    }
}
