package se.jensen.ali.backend;

import se.jensen.ali.backend.model.Post;
import se.jensen.ali.backend.model.User;
import se.jensen.ali.backend.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.jensen.ali.backend.service.PostService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post mockPost;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockPost = new Post();
        mockPost.setId(1L);
        mockPost.setContent("Testinlägg");

        // Skapa mock-användare men STUBBA INTE här
        mockUser = mock(User.class);
        // Stubbingen flyttas till de tester som behöver den
    }

    @Test
    void getAllPosts_ShouldReturnListOfPosts() {
        // ARRANGE
        Post post2 = new Post();
        post2.setId(2L);
        post2.setContent("Andra inlägget");

        List<Post> mockPosts = Arrays.asList(mockPost, post2);
        when(postRepository.findAll()).thenReturn(mockPosts);

        // ACT
        List<Post> result = postService.getAllPosts();

        // ASSERT
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Testinlägg", result.get(0).getContent());
        assertEquals(2L, result.get(1).getId());

        verify(postRepository, times(1)).findAll();
    }

    @Test
    void getPostById_WhenPostExists_ShouldReturnPost() {
        // ARRANGE
        Long postId = 1L;
        when(postRepository.findById(postId))
                .thenReturn(Optional.of(mockPost));

        // ACT
        Post result = postService.getPostById(postId);

        // ASSERT
        assertNotNull(result);
        assertEquals(postId, result.getId());
        assertEquals("Testinlägg", result.getContent());

        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void getPostById_WhenPostDoesNotExist_ShouldReturnNull() {
        // ARRANGE
        Long postId = 999L;
        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        // ACT
        Post result = postService.getPostById(postId);

        // ASSERT
        assertNull(result);

        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void createPost_ShouldSaveAndReturnPost() {
        // ARRANGE
        Post newPost = new Post();
        newPost.setContent("Nytt inlägg");

        // Stubba mockUser här (inte i setUp)
        when(mockUser.getUsername()).thenReturn("testuser");
        newPost.setUser(mockUser);

        Post savedPost = new Post();
        savedPost.setId(2L);
        savedPost.setContent("Nytt inlägg");
        savedPost.setUser(mockUser);

        when(postRepository.save(newPost)).thenReturn(savedPost);

        // ACT
        Post result = postService.createPost(newPost);

        // ASSERT
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Nytt inlägg", result.getContent());
        assertNotNull(result.getUser());

        verify(postRepository, times(1)).save(newPost);
    }

    @Test
    void createPost_WhenPostIsNull_ShouldThrowIllegalArgumentException() {
        // ACT & ASSERT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> postService.createPost(null)
        );

        assertEquals("Post får inte vara null", exception.getMessage());
        verify(postRepository, never()).save(any());
    }

    @Test
    void createPost_WhenUserIsNull_ShouldThrowIllegalArgumentException() {
        // ARRANGE
        Post newPost = new Post();
        newPost.setContent("Inlägg utan användare");
        // User är null - INGEN mockUser behövs här

        // ACT & ASSERT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> postService.createPost(newPost)
        );

        assertEquals("Post måste ha en användare", exception.getMessage());
        verify(postRepository, never()).save(any());
    }

    @Test
    void updatePost_WhenPostExists_ShouldUpdateAndReturnPost() {
        // ARRANGE
        Long postId = 1L;
        Post postDetails = new Post();
        postDetails.setContent("Uppdaterat innehåll");

        Post existingPost = new Post();
        existingPost.setId(postId);
        existingPost.setContent("Gammalt innehåll");
        // User behövs inte för updatePost-testet

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(existingPost));
        when(postRepository.save(existingPost))
                .thenReturn(existingPost);

        // ACT
        Post result = postService.updatePost(postId, postDetails);

        // ASSERT
        assertNotNull(result);
        assertEquals("Uppdaterat innehåll", result.getContent());

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(existingPost);
    }

    @Test
    void updatePost_WhenPostDoesNotExist_ShouldThrowRuntimeException() {
        // ARRANGE
        Long postId = 999L;
        Post postDetails = new Post();

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> postService.updatePost(postId, postDetails)
        );

        assertEquals("Post finns inte med id: " + postId, exception.getMessage());

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).save(any());
    }

    @Test
    void deletePost_WhenPostExists_ShouldDeletePost() {
        // ARRANGE
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(true);
        doNothing().when(postRepository).deleteById(postId);

        // ACT
        postService.deletePost(postId);

        // ASSERT
        verify(postRepository, times(1)).existsById(postId);
        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    void deletePost_WhenPostDoesNotExist_ShouldThrowRuntimeException() {
        // ARRANGE
        Long postId = 999L;
        when(postRepository.existsById(postId)).thenReturn(false);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> postService.deletePost(postId)
        );

        assertEquals("Post finns inte med id: " + postId, exception.getMessage());

        verify(postRepository, times(1)).existsById(postId);
        verify(postRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAllPosts_WhenEmpty_ShouldReturnEmptyList() {
        // ARRANGE
        when(postRepository.findAll()).thenReturn(Arrays.asList());

        // ACT
        List<Post> result = postService.getAllPosts();

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(postRepository, times(1)).findAll();
    }
}
