package com.example.SAPA.service;

import com.example.SAPA.DTOs.ForumDto;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Models.Forum.ForumEntity;
import com.example.SAPA.Repositories.*;
import com.example.SAPA.mappers.ForumMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final ForumRepository forumRepository;
    private final ForumMapper forumMapper;
    private final UserContextService userContextService;


    public ForumDto.ForumResponse createForum(ForumDto.CreateForumRequest request) {
        UserEntity user = userContextService.getAuthenticatedUser();

        ForumEntity forum = ForumEntity.builder()
                .createdBy(user)
                .title(request.title())
                .description(request.description())
                .build();

        ForumEntity saved = forumRepository.save(forum);
        return forumMapper.toForumResponse(saved, userContextService.resolveName(user));
    }


    public ForumDto.ForumResponse updateForum(Long forumId, ForumDto.UpdateForumRequest request) {
        UserEntity user = userContextService.getAuthenticatedUser();

        ForumEntity forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new EntityNotFoundException("Foro no encontrado con id: " + forumId));

        if (!forum.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("No tenés permiso para modificar este foro");
        }

        forum.setTitle(request.title());
        forum.setDescription(request.description());

        ForumEntity updated = forumRepository.save(forum);
        return forumMapper.toForumResponse(updated, userContextService.resolveName(user));
    }


    public void deleteForum(Long forumId) {
        UserEntity user = userContextService.getAuthenticatedUser();

        ForumEntity forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new EntityNotFoundException("Foro no encontrado con id: " + forumId));

        if (!forum.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("No tenés permiso para eliminar este foro");
        }

        forum.setActive(false);
        forumRepository.save(forum);
    }


    public List<ForumDto.ForumResponse> getAllForums() {
        return forumRepository.findByActiveTrue()
                .stream()
                .map(f -> forumMapper.toForumResponse(f, userContextService.resolveName(f.getCreatedBy())))
                .toList();
    }


    public List<ForumDto.ForumResponse> filterForums(String title) {
        return forumRepository.findByActiveTrueAndTitleContainingIgnoreCase(title)
                .stream()
                .map(f -> forumMapper.toForumResponse(f, userContextService.resolveName(f.getCreatedBy())))
                .toList();
    }
}
