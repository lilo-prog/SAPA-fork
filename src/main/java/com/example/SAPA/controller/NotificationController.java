package com.example.SAPA.controller;

import com.example.SAPA.Models.NotificationEntity;
import com.example.SAPA.exceptions.EmptyCollectionException;
import com.example.SAPA.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notificaciones")
@CrossOrigin("*")
public class NotificationController {
    //Atributos
    public NotificationService notificationService;
    //Constructor
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    //Metodos
    @GetMapping
    public List<NotificationEntity> getNotifications() throws EmptyCollectionException{
        return notificationService.getAllNotifications();
    }
    @GetMapping("/{id}")
    public List<NotificationEntity> getNotificationByUserId(@PathVariable Long user_id) throws EmptyCollectionException{
        return notificationService.getNotificationsByUserId(user_id);
    }

}
