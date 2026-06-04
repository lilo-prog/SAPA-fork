package com.example.SAPA.service;

import com.example.SAPA.Models.NotificationEntity;
import com.example.SAPA.Repositories.NotificationRepository;
import com.example.SAPA.exceptions.EmptyCollectionException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    //Atributos
    private NotificationRepository notificationRepository;
    private UserService userService ;
    //Constructor
    public NotificationService(NotificationRepository notificationRepository,UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }
    //Metodos
    public void validateNotifications() throws EmptyCollectionException {
        if(notificationRepository.count() < 0) throw new EmptyCollectionException("No hay notificaciones");
    }
    public void validateId(Long id) throws EmptyCollectionException{
        validateNotifications();
        if(id<0 || id>notificationRepository.count()) throw new IllegalArgumentException("ID invalido");
    }
    public List<NotificationEntity> getAllNotifications() throws EmptyCollectionException{
        validateNotifications();
        return notificationRepository.findAll();
    }
    public Optional<NotificationEntity> getNotificationById(Long id) throws EmptyCollectionException{
        validateId(id);
        return Optional.of(notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Notificacion no encontrada")));
    }
    public String saveNotification(NotificationEntity notificationEntity) throws EmptyCollectionException{
        validateNotifications();
        notificationRepository.save(notificationEntity);
        return "Notificacion guardada correctamente";
    }
    public String deleteNotificationById(Long id) throws EmptyCollectionException{
        Optional<NotificationEntity> noti = getNotificationById(id);
        noti.ifPresent(notificationEntity -> notificationRepository.delete(notificationEntity));
        return "Notificacion eliminada correctamente";
    }
    public List<NotificationEntity> getNotificationsByUserId(Long id) throws EmptyCollectionException{
        if(userService.getAllUsers())
    }
}
