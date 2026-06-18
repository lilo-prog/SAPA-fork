package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Response.NotificationResponseDTO;
import com.example.SAPA.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Operaciones para consultar el historial de alertas, marcar avisos como leídos y depurar la bandeja de entrada del usuario")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
            summary = "Obtener todas mis notificaciones",
            description = "Recupera el historial completo de alertas (leídas y no leídas) pertenecientes al usuario autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de notificaciones obtenido correctamente.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token de sesión inválido o expirado.", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications() {
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }

    @Operation(
            summary = "Obtener notificaciones no leídas",
            description = "Filtra la bandeja de entrada y devuelve exclusivamente las alertas que el usuario todavia tiene pendientes por revisar."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de notificaciones no leídas recuperada con éxito.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Sesión inválida.", content = @Content)
    })
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotifications() {
        return ResponseEntity.ok(notificationService.getUnreadNotifications());
    }

    @Operation(
            summary = "Marcar notificación como leída",
            description = "Cambia el estado de una notificación específica a 'leída' utilizando su ID único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación actualizada de forma exitosa.",
                    content = @Content(schema = @Schema(implementation = NotificationResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - No puedes modificar una notificación que no te pertenece.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID de la notificación especificado no fue encontrado.", content = @Content)
    })
    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDTO> markAsRead(
            @Parameter(description = "ID único de la notificación", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @Operation(
            summary = "Eliminar una notificación",
            description = "Remueve de forma permanente una alerta de la bandeja de entrada del usuario a través de su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificación eliminada con éxito."),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes autorización para borrar este recurso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID de la notificación no coincide con ningún registro activo.", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "ID de la notificación que se desea eliminar", required = true) @PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
