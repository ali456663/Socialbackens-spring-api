package se.jensen.ali.backend.controller;

import se.jensen.ali.backend.model.User;  // ÄNDRA HÄR
import se.jensen.ali.backend.service.UserService;  // ÄNDRA HÄR
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController{

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        System.out.println("📝 Hämtar alla användare");
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("ℹ️ Inga användare hittades");
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)  // 204
                    .build();
        }

        System.out.println("✅ Hittade " + users.size() + " användare");
        return ResponseEntity.ok(users);  // 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        System.out.println("📝 Hämtar användare med ID: " + id);
        User user = userService.getUserById(id);

        if (user == null) {
            System.out.println("❌ Användare hittades inte med ID: " + id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        System.out.println("✅ Användare hittad: " + user.getUsername());
        return ResponseEntity.ok(user);  // 200
    }


    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        System.out.println("📝 Skapar ny användare: " + user.getUsername());

        // Kontrollera om användarnamn finns
        if (userService.usernameExists(user.getUsername())) {
            System.out.println("⚠️ Användarnamn finns redan: " + user.getUsername());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)  // 409 Conflict
                    .build();
        }

        User createdUser = userService.createUser(user);
        System.out.println("✅ Användare skapad med ID: " + createdUser.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)  // 201
                .body(createdUser);
    }
}
