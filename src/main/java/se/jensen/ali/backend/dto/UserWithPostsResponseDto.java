package se.jensen.ali.backend.dto;


import java.util.List;

public class UserWithPostsResponseDto {
    private Long id;
    private String username;
    private List<PostResponseDto> posts;

    public UserWithPostsResponseDto() {}

    public UserWithPostsResponseDto(Long id, String username, List<PostResponseDto> posts) {
        this.id = id;
        this.username = username;
        this.posts = posts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<PostResponseDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponseDto> posts) {
        this.posts = posts;
    }
}
