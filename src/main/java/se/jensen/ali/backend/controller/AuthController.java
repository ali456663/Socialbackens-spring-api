package se.jensen.ali.backend.controller;



import se.jensen.ali.backend.dto.LoginRequest;
import se.jensen.ali.backend.dto.LoginResponse;
import se.jensen.ali.backend.model.User;
import se.jensen.ali.backend.repository.UserRepository;
import se.jensen.ali.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Registrerar en ny användare i systemet.
     * Skapar användarkonto och returnerar JWT token.
     *
     * @param user Användaren att registrera
     * @return ResponseEntity med JWT token och meddelande
     *         - 201 Created vid lyckad registrering
     *         - 400 Bad Request om användarnamn redan finns
     */
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody User user) {
        System.out.println("📝 Försöker registrera användare: " + user.getUsername());

        // Kolla om användarnamn finns
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            System.out.println("⚠️ Användarnamn finns redan: " + user.getUsername());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(
                            null,
                            "Användarnamn finns redan",
                            null
                    ));
        }

        // Kryptera lösenord
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Spara användare
        User savedUser = userRepository.save(user);
        System.out.println("✅ Användare sparad med ID: " + savedUser.getId());

        // Skapa token
        String token = jwtUtil.generateToken(savedUser.getUsername());
        System.out.println("🔑 Token skapad för: " + savedUser.getUsername());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new LoginResponse(
                        token,
                        "Registrering lyckades",
                        savedUser.getId()
                ));
    }

    /**
     * Loggar in en befintlig användare.
     * Verifierar användarnamn och lösenord, returnerar JWT token.
     *
     * @param loginRequest Inloggningsuppgifter
     * @return ResponseEntity med JWT token
     *         - 200 OK vid lyckad inloggning
     *         - 401 Unauthorized vid felaktiga uppgifter
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("📝 Försöker logga in: " + loginRequest.getUsername());

        // Hitta användare
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElse(null);

        if (user == null) {
            System.out.println("❌ Användare hittades inte: " + loginRequest.getUsername());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(
                            null,
                            "Fel användarnamn eller lösenord",
                            null
                    ));
        }

        // Kontrollera lösenord
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("❌ Fel lösenord för: " + loginRequest.getUsername());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(
                            null,
                            "Fel användarnamn eller lösenord",
                            null
                    ));
        }

        // Skapa token
        String token = jwtUtil.generateToken(user.getUsername());
        System.out.println("✅ Inloggning lyckad för: " + user.getUsername());

        return ResponseEntity
                .ok()
                .body(new LoginResponse(
                        token,
                        "Inloggning lyckades",
                        user.getId()
                ));
    }
}
