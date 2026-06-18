package com.example.SAPA.controller;

import com.example.SAPA.DTOs.ChatDTO;
import com.example.SAPA.service.AttachmentService;
import com.example.SAPA.service.ConversationService;
import com.example.SAPA.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/conversations")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(
        name = "Chat y Mensajería",
        description = "Operaciones para consultar bandejas de entrada, historiales de chat y transferencia de archivos adjuntos. " +
                "El envío de mensajes en tiempo real se gestiona por WebSockets (STOMP) mediante el destino '/app/chat/{conversationId}'."
)
public class ChatController {

    private final ConversationService conversationService;
    private final MessageService messageService;
    private final AttachmentService attachmentService;

    @MessageMapping("/chat/{conversationId}")
    public void sendMessage(
            @DestinationVariable Long conversationId,
            ChatDTO.SendMessageRequest request,
            Principal principal
    ) {
        messageService.sendMessage(conversationId, request, principal);
    }

    @Operation(
            summary = "Obtener conversaciones del usuario",
            description = "Devuelve el listado completo y resumido de las conversaciones activas asociadas al usuario autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de conversaciones obtenido con éxito.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatDTO.ConversationSummary.class))))
    })
    @GetMapping
    public ResponseEntity<List<ChatDTO.ConversationSummary>> getMyConversations() {
        return ResponseEntity.ok(conversationService.getMyConversations());
    }

    @Operation(
            summary = "Obtener historial de mensajes",
            description = "Recupera de forma cronológica la lista de mensajes intercambiados dentro de una conversación específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de conversación recuperado correctamente.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatDTO.MessageResponse.class)))),
            @ApiResponse(responseCode = "404", description = "La conversación no existe o el usuario no pertenece a ella.", content = @Content)
    })
    @GetMapping("/{id}/messages")
    public ResponseEntity<List<ChatDTO.MessageResponse>> getHistory(
            @Parameter(description = "ID único de la conversación", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(conversationService.getConversationHistory(id));
    }

    @Operation(
            summary = "Subir y enviar archivo adjunto",
            description = "Permite cargar un archivo (imagen, documento, pdf, etc.) mediante un formulario multipart para enviarlo como mensaje dentro del chat."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo subido y enviado exitosamente como mensaje.",
                    content = @Content(schema = @Schema(implementation = ChatDTO.MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "El archivo enviado está corrupto o supera el tamaño límite permitido.", content = @Content),
            @ApiResponse(responseCode = "404", description = "La conversación especificada no fue encontrada.", content = @Content)
    })
    @PostMapping(value = "/{id}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ChatDTO.MessageResponse> uploadAttachment(
            @Parameter(description = "ID de la conversación donde se adjuntará el archivo", required = true) @PathVariable Long id,
            @Parameter(description = "Archivo binario que se va a subir", required = true) @RequestParam("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(attachmentService.uploadAttachment(id, file));
    }
}
