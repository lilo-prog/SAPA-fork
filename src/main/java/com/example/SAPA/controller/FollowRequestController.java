package com.example.SAPA.controller;

import com.example.SAPA.Models.FollowRequestEntity;
import com.example.SAPA.service.FollowRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/follow-requests")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Solicitudes de Seguimiento", description = "Operaciones para gestionar el vínculo de seguimiento doctor entre pacientes y doctores")
public class FollowRequestController {

    private final FollowRequestService followRequestService;

    @Operation(
            summary = "Enviar solicitud de seguimiento",
            description = "Permite a un paciente enviar una solicitud de seguimiento a un doctor específico mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud de seguimiento creada exitosamente.",
                    content = @Content(schema = @Schema(implementation = FollowRequestEntity.class))),
            @ApiResponse(responseCode = "400", description = "Ya existe una solicitud pendiente o un vínculo activo con este doctor.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del doctor proporcionado no existe.", content = @Content)
    })
    @PostMapping("/send/{doctorId}")
    public ResponseEntity<FollowRequestEntity> create(
            @Parameter(description = "ID del doctor a quien se le envía la solicitud", required = true) @PathVariable Long doctorId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(followRequestService.create(doctorId));
    }

    @Operation(
            summary = "Aprobar solicitud de seguimiento",
            description = "Permite a un doctor aceptar una solicitud pendiente para formalizar el vínculo con el paciente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud aprobada y vínculo establecido exitosamente.",
                    content = @Content(schema = @Schema(implementation = FollowRequestEntity.class))),
            @ApiResponse(responseCode = "404", description = "La solicitud de seguimiento no fue encontrada.", content = @Content)
    })
    @PatchMapping("/{id}/approve")
    public ResponseEntity<FollowRequestEntity> approve(
            @Parameter(description = "ID de la solicitud de seguimiento", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(followRequestService.approve(id));
    }

    @Operation(
            summary = "Rechazar solicitud de seguimiento",
            description = "Permite a un doctor denegar una solicitud de seguimiento recibida de un paciente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud rechazada correctamente.",
                    content = @Content(schema = @Schema(implementation = FollowRequestEntity.class))),
            @ApiResponse(responseCode = "404", description = "La solicitud de seguimiento no fue encontrada.", content = @Content)
    })
    @PatchMapping("/{id}/reject")
    public ResponseEntity<FollowRequestEntity> reject(
            @Parameter(description = "ID de la solicitud de seguimiento", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(followRequestService.reject(id));
    }

    @Operation(
            summary = "Disolver o cancelar vínculo doctor-paciente",
            description = "Rompe la relación de seguimiento activa entre el doctor y el paciente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vínculo de seguimiento disuelto exitosamente.",
                    content = @Content(schema = @Schema(implementation = FollowRequestEntity.class))),
            @ApiResponse(responseCode = "44", description = "La solicitud o el vínculo activo no existen.", content = @Content)
    })
    @PatchMapping("/{id}/dissolve")
    public ResponseEntity<FollowRequestEntity> dissolve(
            @Parameter(description = "ID de la solicitud/vínculo a disolver", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(followRequestService.dissolve(id));
    }

    @Operation(
            summary = "Obtener solicitudes pendientes recibidas",
            description = "Recupera la lista de todas las solicitudes de seguimiento pendientes de aprobación que han sido enviadas al usuario actual."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de solicitudes pendientes recuperada correctamente.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FollowRequestEntity.class))))
    })
    @GetMapping("/pending")
    public ResponseEntity<List<FollowRequestEntity>> getPendingRequests() {
        return ResponseEntity.ok(followRequestService.getPendingRequests());
    }

    @Operation(
            summary = "Obtener solicitudes enviadas",
            description = "Recupera la lista de solicitudes de seguimiento que el usuario actual ha emitido hacia otros profesionales."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de solicitudes enviadas recuperada correctamente.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FollowRequestEntity.class))))
    })
    @GetMapping("/sent")
    public ResponseEntity<List<FollowRequestEntity>> getSentRequests() {
        return ResponseEntity.ok(followRequestService.getSentRequests());
    }
}
