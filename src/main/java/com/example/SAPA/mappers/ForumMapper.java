package com.example.SAPA.mappers;

import com.example.SAPA.DTOs.ForumDto;
import com.example.SAPA.Models.Forum.ForumEntity;
import com.example.SAPA.Models.Forum.PostEntity;
import com.example.SAPA.Models.Forum.SavedPostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ForumMapper {

    @Mapping(source = "forum.id", target = "forumId")
    @Mapping(source = "createdByName", target = "createdByName")
    @Mapping(source = "forum.title", target = "title")
    @Mapping(source = "forum.description", target = "description")
    @Mapping(source = "forum.active", target = "active")
    @Mapping(source = "forum.createdAt", target = "createdAt")
    ForumDto.ForumResponse toForumResponse(ForumEntity forum, String createdByName);


    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "post.forum.id", target = "forumId")
    @Mapping(source = "post.forum.title", target = "forumTitle")
    @Mapping(source = "authorName", target = "authorName")
    @Mapping(source = "post.title", target = "title")
    @Mapping(source = "post.content", target = "content")
    @Mapping(source = "post.active", target = "active")
    @Mapping(source = "post.createdAt", target = "createdAt")
    ForumDto.PostResponse toPostResponse(PostEntity post, String authorName);


    @Mapping(source = "saved.id", target = "savedPostId")
    @Mapping(source = "saved.savedAt", target = "savedAt")
    @Mapping(target = "post", expression = "java(toPostResponse(saved.getPost(), authorName))")
    ForumDto.SavedPostResponse toSavedPostResponse(SavedPostEntity saved, String authorName);
}
