package se.jensen.ali.backend.config;

import se.jensen.ali.backend.model.User;
import se.jensen.ali.backend.model.Post;
import se.jensen.ali.backend.repository.UserRepository;
import se.jensen.ali.backend.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            PostRepository postRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            System.out.println("🚀🚀🚀 DATA INITIALIZER STARTAR 🚀🚀🚀");

            // Kontrollera om data redan finns
            long userCount = userRepository.count();
            long postCount = postRepository.count();

            System.out.println("📊 Innan init: " + userCount + " users, " + postCount + " posts");


            if (userCount == 0) {
                System.out.println("🧹 Rensar gamla data...");
                postRepository.deleteAll();
                userRepository.deleteAll();

                System.out.println("👤 Skapar testanvändare...");

                User user1 = new User();
                user1.setUsername("ali");
                user1.setPassword(passwordEncoder.encode("password123"));  // VIKTIGT: Kryptera lösenordet!

                User user2 = new User();
                user2.setUsername("lok");
                user2.setPassword(passwordEncoder.encode("password123"));  // VIKTIGT: Kryptera lösenordet!

                User savedUser1 = userRepository.save(user1);
                User savedUser2 = userRepository.save(user2);
                System.out.println("✅ Användare sparade: ali och lok");

                System.out.println("📝 Skapar testposts...");

                Post post1 = new Post();
                post1.setContent("Hej, detta är min första post!");
                post1.setUser(savedUser1);

                Post post2 = new Post();
                post2.setContent("Vad trevligt att se er här!");
                post2.setUser(savedUser2);

                Post post3 = new Post();
                post3.setContent("En till post från Ali");
                post3.setUser(savedUser1);

                postRepository.save(post1);
                postRepository.save(post2);
                postRepository.save(post3);
                System.out.println("✅ 3 posts sparade");

                for (int i = 4; i <= 6; i++) {
                    Post post = new Post();
                    post.setContent("Automatisk post #" + i + " från lok");
                    post.setUser(savedUser2);
                    postRepository.save(post);
                    System.out.println("➕ Post #" + i + " skapad");
                }

                System.out.println("🎉🎉🎉 Testdata skapad klart! 🎉🎉🎉");
                System.out.println("   - 2 användare: ali (id: " + savedUser1.getId() + "), lok (id: " + savedUser2.getId() + ")");
                System.out.println("   - 6 posts totalt");
                System.out.println("🌐 API är redo på http://localhost:9090");
                System.out.println("🔧 H2 Console: http://localhost:9090/h2-console");
                System.out.println("   JDBC URL: jdbc:h2:mem:socialdb");
                System.out.println("   Username: sa");
                System.out.println("   Password: password");
            } else {
                System.out.println("ℹ️ Data finns redan, inget skapas");
                System.out.println("   Användare: " + userCount);
                System.out.println("   Posts: " + postCount);
            }

            System.out.println("🚀🚀🚀 DATA INITIALIZER AVSLUTAD 🚀🚀🚀");
        };
    }
}

