package se.jensen.ali.backend.dto;


public class PostRequestDto {
    private String content;

    public PostRequestDto() {}

    public PostRequestDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
