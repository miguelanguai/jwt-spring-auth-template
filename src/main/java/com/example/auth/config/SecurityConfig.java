package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.auth.authentication.JwtAuthenticationFilter;
import com.example.auth.authentication.UserDetailsServiceImpl;

/**
 * The SecurityConfig class is a Spring Security configuration class. It
 * configures security settings such as authentication, JWT token filtering, and
 * custom logout handling for the application.
 * 
 * This class is annotated with {@link Configuration} and
 * {@link EnableWebSecurity}: - {@link Configuration}: Indicates that this class
 * provides Spring configuration. - {@link EnableWebSecurity}: Enables Spring
 * Security’s web security support and provides the Spring MVC integration.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsServiceImp;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomLogoutHandler logoutHandler;

    public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImp, JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomLogoutHandler logoutHandler) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.logoutHandler = logoutHandler;
    }

    /**
     * Configures the security filter chain for handling HTTP requests and security
     * concerns.
     * 
     * This method defines how incoming requests are secured using JWT
     * authentication, session management, and specific authorization rules based on
     * roles (e.g., "ADMIN", "USER"). It also configures custom handling for access
     * denied and authentication failure cases.
     * 
     * @param http The {@link HttpSecurity} object provided by Spring Security to
     *             configure web-based security for specific HTTP requests.
     * @return A configured {@link SecurityFilterChain} that defines the
     *         application's security filter chain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(req -> req.requestMatchers("/login/**", "/register/**", "/p/**").permitAll()

                        .requestMatchers("/a/**").hasAuthority("ADMIN").requestMatchers("/u/**")
                        .hasAnyAuthority("ADMIN", "USER").requestMatchers("/ua/**").hasAnyAuthority("ADMIN", "USER")
                        .anyRequest().authenticated())

                .userDetailsService(userDetailsServiceImp)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(e -> e
                        .accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(403))
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

                .logout(l -> l.logoutUrl("/logout").addLogoutHandler(logoutHandler).logoutSuccessHandler(
                        (request, response, authentication) -> SecurityContextHolder.clearContext()))

                .build();

    }

    /**
     * Defines a bean for the {@link PasswordEncoder} used to hash passwords in the
     * application.
     * 
     * This method returns an instance of {@link BCryptPasswordEncoder}, which is a
     * widely-used password hashing algorithm that automatically handles the
     * complexity of securely encoding and verifying passwords.
     * 
     * By configuring this bean, Spring Security can use it for encoding passwords
     * during user registration and verifying them during authentication.
     * 
     * @return A {@link BCryptPasswordEncoder} instance that will be used for
     *         password encoding and verification.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines a bean for the {@link AuthenticationManager} used in the application.
     * 
     * The {@link AuthenticationManager} is a core component in Spring Security
     * responsible for processing authentication requests. It delegates the
     * authentication process to the appropriate {@link AuthenticationProvider}
     * based on the provided credentials.
     * 
     * This method retrieves the default {@link AuthenticationManager} from the
     * {@link AuthenticationConfiguration}, which is configured automatically by
     * Spring Security based on the application's security setup.
     * 
     * @param configuration The {@link AuthenticationConfiguration} object that
     *                      holds the security configuration for the application.
     * @return The {@link AuthenticationManager} instance used to authenticate
     *         users.
     * @throws Exception If there is an issue retrieving the
     *                   {@link AuthenticationManager}.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
