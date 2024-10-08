package com.example.auth.authentication;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * This method is part of a filter that intercepts HTTP requests to handle JWT
     * authentication. It extracts the JWT from the "Authorization" header and
     * validates the token. If valid, it sets the authenticated user in the
     * SecurityContext. Otherwise, the request is passed to the next filter in the
     * chain.
     *
     * <ol>
     * <li>It checks if the "Authorization" header is present and starts with
     * "Bearer".</li>
     * <li>If the header is invalid or missing, the request is passed to the next
     * filter without processing.</li>
     * <li>If the JWT token is found, it extracts the username from the token.</li>
     * <li>If no authentication exists in the current SecurityContext and the token
     * is valid, the user is authenticated based on the token and their user
     * details.</li>
     * <li>The authentication token is created and set in the SecurityContext.</li>
     * <li>The request proceeds through the filter chain.</li>
     * </ol>
     *
     * @param request     The HTTP request being processed.
     * @param response    The HTTP response being generated.
     * @param filterChain The filter chain used to pass the request to the next
     *                    filter.
     * @throws ServletException If an exception occurs during request processing.
     * @throws IOException      If an I/O exception occurs during request
     *                          processing.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); // Extract the Authorization header from the request

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // If no header or invalid token, pass the request to the next filter
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // Extract the JWT token
        String username = jwtService.extractUsername(token); // Extract the username from the JWT token

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details and validate the token if no existing authentication is
            // found in the context
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isValid(token, userDetails)) {
                // Create an authentication token and set it in the SecurityContext
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pass the request to the next filter in the chain
        filterChain.doFilter(request, response);
    }

}
