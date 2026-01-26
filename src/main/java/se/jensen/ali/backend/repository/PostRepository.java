package se.jensen.ali.backend.repository;


import se.jensen.ali.backend.model.Post;  // HÄR ÄR FIXEN!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    List<Post> findAllByOrderByCreatedAtDesc();
}
