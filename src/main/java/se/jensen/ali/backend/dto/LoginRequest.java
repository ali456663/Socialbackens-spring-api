package se.jensen.ali.backend.dto;

import lombok.Data;

/**
 * Data Transfer Object för inloggningsförfrågningar.
 * Används när en användare försöker logga in.
 *
 * @author ali
 * @version 1.0
 */
@Data
public class LoginRequest {
    /**
     * Användarens användarnamn.
     */
    private String username;

    /**
     * Användarens lösenord.
     */
    private String password;
}
