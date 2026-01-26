package se.jensen.ali.backend.service;

import se.jensen.ali.backend.dto.PostResponseDto;
import se.jensen.ali.backend.model.Post;
import se.jensen.ali.backend.model.User;
import se.jensen.ali.backend.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostDataService {

    private static final Logger logger = LoggerFactory.getLogger(PostDataService.class);

    private final PostRepository postRepository;

    public PostDataService(PostRepository postRepository) {
        this.postRepository = postRepository;
        logger.debug("PostDataService initialized");
    }

    public List<PostResponseDto> getPostsForUser(User user) {
        logger.debug("Getting posts for user: {}", user.getUsername());

        List<Post> posts = postRepository.findByUserId(user.getId());
        posts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));

        logger.debug("Found {} posts for user: {}", posts.size(), user.getUsername());

        return posts.stream()
                .map(post -> new PostResponseDto(
                        post.getClass(),
                        post.getContent(),
                        post.getCreatedAt(),
                        user.getId(),
                        user.getUsername()
                ))
                .collect(Collectors.toList());
    }

    public List<PostResponseDto> getAllPostsSorted() {
        logger.debug("Getting all posts sorted by date");

        List<PostResponseDto> posts = postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(post -> new PostResponseDto(
                        post.getId(),
                        post.getContent(),
                        post.getCreatedAt(),
                        post.getUser().getId(),
                        post.getUser().getUsername()
                ))
                .collect(Collectors.toList());

        logger.debug("Found {} total posts", posts.size());
        return posts;
    }
}
