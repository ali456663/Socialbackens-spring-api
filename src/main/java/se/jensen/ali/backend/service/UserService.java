package se.jensen.ali.backend.service;



import se.jensen.ali.backend.model.User;
import se.jensen.ali.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        logger.debug("Hämtar alla användare");
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        logger.debug("Hämtar användare med id: {}", id);

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            logger.warn("Användare hittades inte med id: {}", id);
        } else {
            logger.info("Användare hittad: {} (id: {})",
                    user.getUsername(), user.getId());
        }

        return user;
    }

    public boolean usernameExists(String username) {
        logger.debug("Kontrollerar om användarnamn finns: {}", username);
        return userRepository.findByUsername(username).isPresent();
    }

    public User createUser(User user) {
        logger.info("Skapar ny användare: {}", user.getUsername());

        // Kryptera lösenord
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        logger.debug("Användare skapad med id: {}", savedUser.getId());

        return savedUser;
    }
}
