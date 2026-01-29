package se.jensen.ali.backend.dto;

import lombok.Data;

/**
 * Data Transfer Object för inloggningssvar.
 * Returneras vid lyckad registrering eller inloggning.
 *
 * @author Ditt Namn
 * @version 1.0
 */
@Data
public class LoginResponse {
    /**
     * JWT token för autentisering.
     */
    private String token;

    /**
     * Meddelande om operationens resultat.
     */
    private String message;

    /**
     * Användarens ID.
     */
    private Long userId;

    /**
     * Skapar ett nytt LoginResponse.
     *
     * @param token JWT token
     * @param message Meddelande
     * @param userId Användarens ID
     */
    public LoginResponse(String token, String message, Long userId) {
        this.token = token;
        this.message = message;
        this.userId = userId;
    }
}
