package se.jensen.ali.backend.controller;


import se.jensen.ali.backend.model.Post;  // ÄNDRA HÄR
import se.jensen.ali.backend.service.PostService;  // ÄNDRA HÄR
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class  PostController{

    private final PostService postService;

    // GET alla posts
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        System.out.println("📝 Hämtar alla posts");
        List<Post> posts = postService.getAllPosts();

        if (posts.isEmpty()) {
            System.out.println("ℹ️ Inga posts hittades");
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)  // 204 No Content
                    .build();
        }

        System.out.println("✅ Hittade " + posts.size() + " posts");
        return ResponseEntity.ok(posts);  // 200 OK
    }

    // GET en specifik post
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        System.out.println("📝 Hämtar post med ID: " + id);
        Post post = postService.getPostById(id);

        if (post == null) {
            System.out.println("❌ Post hittades inte med ID: " + id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)  // 404 Not Found
                    .build();
        }

        System.out.println("✅ Post hittad: " + post.getId());
        return ResponseEntity.ok(post);  // 200 OK
    }

    // POST skapa ny post
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        System.out.println("📝 Skapar ny post för användare: " +
                (post.getUser() != null ? post.getUser().getId() : "unknown"));

        try {
            Post createdPost = postService.createPost(post);
            System.out.println("✅ Post skapad med ID: " + createdPost.getId());

            return ResponseEntity
                    .status(HttpStatus.CREATED)  // 201 Created
                    .body(createdPost);
        } catch (Exception e) {
            System.out.println("❌ Kunde inte skapa post: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)  // 400 Bad Request
                    .build();
        }
    }

    // PUT uppdatera post
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @RequestBody Post postDetails) {

        System.out.println("📝 Uppdaterar post med ID: " + id);

        Post existingPost = postService.getPostById(id);
        if (existingPost == null) {
            System.out.println("❌ Post finns inte med ID: " + id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)  // 404
                    .build();
        }

        // Uppdatera innehåll
        existingPost.setContent(postDetails.getContent());

        Post updatedPost = postService.createPost(existingPost);
        System.out.println("✅ Post uppdaterad: " + updatedPost.getId());

        return ResponseEntity.ok(updatedPost);  // 200 OK
    }

    // DELETE ta bort post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        System.out.println("📝 Tar bort post med ID: " + id);

        Post post = postService.getPostById(id);
        if (post == null) {
            System.out.println("❌ Post finns inte med ID: " + id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)  // 404
                    .build();
        }

        postService.deletePost(id);
        System.out.println("✅ Post borttagen: " + id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)  // 204 No Content
                .build();
    }
}

