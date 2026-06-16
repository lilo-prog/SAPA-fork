package com.example.SAPA.DTOs;

import java.time.LocalDateTime;

public class ForumDto {

    public record CreateForumRequest(
            String title,
            String description
    ) {}

    public record UpdateForumRequest(
            String title,
            String description
    ) {}

    public record CreatePostRequest(
            String title,
            String content
    ) {}

    public record UpdatePostRequest(
            String title,
            String content
    ) {}



    public record ForumResponse(
            Long forumId,
            String createdByName,
            String title,
            String description,
            boolean active,
            LocalDateTime createdAt
    ) {}

    public record PostResponse(
            Long postId,
            Long forumId,
            String forumTitle,
            String authorName,
            String title,
            String content,
            boolean active,
            LocalDateTime createdAt
    ) {}

    public record SavedPostResponse(
            Long savedPostId,
            PostResponse post,
            LocalDateTime savedAt
    ) {}
}
