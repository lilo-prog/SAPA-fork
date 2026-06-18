package com.example.SAPA.service;

import com.example.SAPA.DTOs.ForumDto;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Models.Forum.PostEntity;
import com.example.SAPA.Models.Forum.SavedPostEntity;
import com.example.SAPA.Repositories.*;
import com.example.SAPA.mappers.ForumMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedPostService {

    private final PostRepository postRepository;
    private final SavedPostRepository savedPostRepository;
    private final ForumMapper forumMapper;
    private final UserContextService userContextService;


    public ForumDto.SavedPostResponse savePost(Long postId) {
        UserEntity user = userContextService.getAuthenticatedUser();

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post no encontrado con id: " + postId));

        boolean alreadySaved = savedPostRepository
                .findByUserEntityAndPost(user, post)
                .isPresent();

        if (alreadySaved) {
            throw new RuntimeException("Ya tenés este post guardado en favoritos");
        }

        SavedPostEntity saved = SavedPostEntity.builder()
                .userEntity(user)
                .post(post)
                .build();

        SavedPostEntity result = savedPostRepository.save(saved);
        return forumMapper.toSavedPostResponse(result, userContextService.resolveName(post.getAuthor()));
    }


    public void unsavePost(Long postId) {
        UserEntity user = userContextService.getAuthenticatedUser();

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post no encontrado con id: " + postId));

        SavedPostEntity saved = savedPostRepository.findByUserEntityAndPost(user, post)
                .orElseThrow(() -> new RuntimeException("No tenés este post guardado en favoritos"));

        savedPostRepository.delete(saved);
    }

    public List<ForumDto.SavedPostResponse> getSavedPosts() {
        UserEntity user = userContextService.getAuthenticatedUser();

        return savedPostRepository.findByUserEntity(user)
                .stream()
                .map(s -> forumMapper.toSavedPostResponse(s, userContextService.resolveName(s.getPost().getAuthor())))
                .toList();
    }
}
