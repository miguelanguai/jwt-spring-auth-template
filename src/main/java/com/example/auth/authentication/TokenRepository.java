package com.example.auth.authentication;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.auth.authentication.model.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    /**
     * Retrieves all tokens the user passed as parameter has
     * 
     * @param userId
     * @return {@link Token}
     */
    @Query("""
            select t from Token t inner join UserEntity u on t.user.id = u.id
            where t.user.id = :userId and t.loggedOut = false
            """)
    List<Token> findAllTokensByUser(Long userId);

    /**
     * find the token that is passed by parameter
     * 
     * @param token
     * @return {@link Token}
     */
    Optional<Token> findByToken(String token);
}
