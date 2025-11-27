package com.dishcovery.backend.dto;

public class CommentDto {
    private String content;
    private Long recipeId;
    private Long parentId;

    public CommentDto() {
    }

    public CommentDto(String content, Long recipeId, Long parentId) {
        this.content = content;
        this.recipeId = recipeId;
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "content='" + content + '\'' +
                ", recipeId=" + recipeId +
                ", userId=" + parentId +
                '}';
    }
}
