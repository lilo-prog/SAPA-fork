package com.example.SAPA.service;

import com.example.SAPA.DTOs.Response.NotificationResponseDTO;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Models.NotificationEntity;
import com.example.SAPA.Repositories.NotificationRepository;
import com.example.SAPA.enums.NotificationType;
import com.example.SAPA.mappers.NotificationMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserContextService userContext;


    public void createNotification(UserEntity user, String title, String msg, NotificationType type) {
        NotificationEntity notification = NotificationEntity.builder()
                .user(user)
                .title(title)
                .msg(msg)
                .type(type)
                .readed(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }


    public List<NotificationResponseDTO> getMyNotifications() {
        UserEntity user = userContext.getAuthenticatedUser();

        return notificationRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }


    public List<NotificationResponseDTO> getUnreadNotifications() {
        UserEntity user = userContext.getAuthenticatedUser();

        return notificationRepository.findByUserAndReadedFalse(user)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }


    public NotificationResponseDTO markAsRead(Long notificationId) {
        UserEntity user = userContext.getAuthenticatedUser();

        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notificación no encontrada con id: " + notificationId));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tenés permiso para modificar esta notificación");
        }

        notification.setReaded(true);
        return notificationMapper.toResponse(notificationRepository.save(notification));
    }


    public void deleteNotification(Long notificationId) {
        UserEntity user = userContext.getAuthenticatedUser();

        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notificación no encontrada con id: " + notificationId));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tenés permiso para eliminar esta notificación");
        }

        notificationRepository.delete(notification);
    }
}
