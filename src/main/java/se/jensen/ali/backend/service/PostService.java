package se.jensen.ali.backend.service;


import se.jensen.ali.backend.model.Post;
import se.jensen.ali.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;

    /**
     * Hämtar alla posts från databasen.
     *
     * @return Lista med alla posts, tom lista om inga finns
     */
    public List<Post> getAllPosts() {
        logger.debug("Hämtar alla posts");
        List<Post> posts = postRepository.findAll();
        logger.info("Hittade {} posts", posts.size());
        return posts;
    }

    /**
     * Hämtar en specifik post med angivet ID.
     *
     * @param id Postens ID att hämta
     * @return Posten med angivet ID, eller null om den inte finns
     */
    public Post getPostById(Long id) {
        logger.debug("Hämtar post med id: {}", id);

        Post post = postRepository.findById(id).orElse(null);

        if (post == null) {
            logger.warn("Post hittades inte med id: {}", id);
        } else {
            logger.info("Post hittad: {} av användare {}",
                    post.getId(),
                    post.getUser() != null ? post.getUser().getUsername() : "okänd");
        }

        return post;
    }

    /**
     * Skapar en ny post i databasen.
     *
     * @param post Posten att skapa
     * @return Den skapade posten med genererat ID
     * @throws IllegalArgumentException om post eller användare är null
     */
    public Post createPost(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post får inte vara null");
        }

        if (post.getUser() == null) {
            throw new IllegalArgumentException("Post måste ha en användare");
        }

        logger.info("Skapar ny post för användare: {}",
                post.getUser().getUsername());

        Post savedPost = postRepository.save(post);
        logger.debug("Post skapad med id: {}", savedPost.getId());

        return savedPost;
    }

    /**
     * Uppdaterar en befintlig post.
     *
     * @param id ID för posten att uppdatera
     * @param postDetails Nya uppgifter för posten
     * @return Den uppdaterade posten
     * @throws RuntimeException om posten inte finns
     */
    public Post updatePost(Long id, Post postDetails) {
        logger.info("Uppdaterar post med id: {}", id);

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post finns inte med id: " + id));

        // Uppdatera fält
        if (postDetails.getContent() != null) {
            existingPost.setContent(postDetails.getContent());
        }

        // Uppdatera andra fält här om de finns i din Post-modell
        if (postDetails.getId() != null) {
            existingPost.setId(postDetails.getId());
        }

        // Observera: Vi uppdaterar INTE user här, då en post vanligtvis
        // inte byter ägare när den uppdateras

        Post updatedPost = postRepository.save(existingPost);
        logger.debug("Post uppdaterad: {}", id);

        return updatedPost;
    }

    /**
     * Tar bort en post med angivet ID.
     *
     * @param id ID för posten att ta bort
     * @throws RuntimeException om posten inte finns
     */
    public void deletePost(Long id) {
        logger.info("Tar bort post med id: {}", id);

        if (!postRepository.existsById(id)) {
            logger.error("Kan inte ta bort post - post finns inte med id: {}", id);
            throw new RuntimeException("Post finns inte med id: " + id);
        }

        postRepository.deleteById(id);
        logger.debug("Post borttagen: {}", id);
    }
}
