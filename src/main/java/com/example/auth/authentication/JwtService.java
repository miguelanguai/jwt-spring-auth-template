package com.example.auth.authentication;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.auth.user.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final String SECRET_KEY = "56d76fa66134bca9f77fb18253673dcea105f56e10b3dd157acf8eff4c337718";

    private final TokenRepository tokenRepository;

    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * returns user name associated to the token
     * 
     * @param token
     * @return el nombre del user
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Verifies token is not expired and it's the same as the one the user has. <br>
     * <p>
     * It gets the current user username and verifies it is the same as the user
     * passed on the parameter.
     * </p>
     * 
     * @param token
     * @param user
     * @return boolean
     */
    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);

        boolean validToken = tokenRepository.findByToken(token).map(t -> !t.getIsLoggedOut()).orElse(false);

        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;
    }

    /**
     * Verifies if token passed as a parameter has expired.
     * 
     * @param token
     * @return si el token ha expirado
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * It gets the expiration of the token
     * 
     * @param token
     * @return {@link Date}
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from a JWT token.
     * 
     * <p>
     * This method retrieves all the claims from the provided JWT token and applies
     * the given resolver function to extract the desired claim.
     * </p>
     *
     * <p>
     * For example, it could be used to extract claims such as the username,
     * expiration time, etc.
     * </p>
     *
     * @param <T>      The type of the claim to be extracted.
     * @param token    The JWT token from which the claim will be extracted.
     * @param resolver A function that processes the {@link Claims} object to
     *                 extract the desired claim.
     * @return The extracted claim of type {@code T}.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token); // Load all claims from the JWT token
        return resolver.apply(claims); // Apply the resolver function to extract the desired claim
    }

    /**
     * Extracts all claims from a JWT token.
     * 
     * <p>
     * This method verifies the JWT token signature using the signing key and parses
     * the claims contained within the token. Claims in a JWT typically include
     * information such as the subject (e.g., username), issued time, expiration
     * time, and custom claims.
     * </p>
     *
     * <p>
     * This method is primarily used internally to provide claims that can be
     * processed by other methods.
     * </p>
     *
     * @param token The JWT token from which all claims will be extracted.
     * @return A {@link Claims} object containing all the claims from the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser() // Begin parsing the JWT token
                .verifyWith(getSigninKey()) // Verify the JWT token signature using the signing key
                .build() // Build the parser
                .parseSignedClaims(token) // Parse the token and retrieve the signed claims
                .getPayload(); // Extract and return the payload (claims) from the token
    }

    /**
     * Generates a JWT token for a given user.
     * 
     * <p>
     * This method creates a JWT (JSON Web Token) using the username of the provided
     * {@link UserEntity}, with the current timestamp as the issued time and a set
     * expiration time of 24 hours from when the token is generated.
     * </p>
     *
     * <p>
     * The token is signed using an HMAC SHA key, which is derived from a secret
     * key. The resulting token is compacted into a string.
     * </p>
     *
     * @param user The {@link UserEntity} for whom the token is being generated. The
     *             username of the user is used as the subject of the token.
     * @return A signed and compacted JWT token as a string.
     */
    public String generateToken(UserEntity user) {
        String token = Jwts.builder() // Builds the JWT token
                .subject(user.getUsername()) // Sets the username of the user as the subject of the token
                .issuedAt(new Date(System.currentTimeMillis())) // Sets the issued time of the token to the current time
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // Sets the expiration time to
                                                                                        // 24 hours from now
                .signWith(getSigninKey()) // Signs the token with the signing key
                .compact(); // Compacts the token into a string

        return token;
    }

    /**
     * Retrieves the signing key for generating JWT tokens.
     * 
     * <p>
     * This method decodes the secret key (stored as a base64-encoded string) and
     * returns a {@link SecretKey} that can be used to sign JWT tokens using the
     * HMAC SHA algorithm.
     * </p>
     *
     * @return A {@link SecretKey} generated from the base64-encoded secret key.
     */
    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY); // Decode the base64-encoded secret key
        return Keys.hmacShaKeyFor(keyBytes); // Return the HMAC SHA signing key
    }

}
