package com.example.auth.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.auth.user.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Loads a user by their username.
     * 
     * <p>
     * This method is used by Spring Security to retrieve the {@link UserDetails}
     * for authentication purposes. It looks up the user by their username in the
     * repository, and if the user is found, their details are returned. If the user
     * is not found, a {@link UsernameNotFoundException} is thrown.
     * </p>
     *
     * @param username The username of the user to be loaded.
     * @return The {@link UserDetails} of the user, which contains the user's
     *         authentication and authorization information.
     * @throws UsernameNotFoundException If the user with the given username is not
     *                                   found in the repository.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
