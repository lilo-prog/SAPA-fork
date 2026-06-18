package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Response.HealthTipResponseDTO;
import com.example.SAPA.Models.HealthTipEntity;
import com.example.SAPA.service.HealthTipService;
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

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/health-tips")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Consejos de Salud", description = "Operaciones para gestionar y consultar recomendaciones o consejos de salud publicados por profesionales")
public class HealthTipController {

    private final HealthTipService healthTipService;

    @Operation(
            summary = "Crear un consejo de salud",
            description = "Permite a un doctor publicar un nuevo consejo o recomendación sanitaria en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consejo de salud creado exitosamente.",
                    content = @Content(schema = @Schema(implementation = HealthTipResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "El formato o los datos de la recomendación son inválidos.", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT ausente o inválido.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<HealthTipResponseDTO> createHealthTip(@RequestBody HealthTipEntity healthTip) {
        return ResponseEntity.status(HttpStatus.CREATED).body(healthTipService.createHealthTip(healthTip));
    }

    @Operation(
            summary = "Actualizar un consejo de salud",
            description = "Modifica los datos de un consejo existente mediante su ID. El emisor debe tener los permisos correspondientes."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consejo de salud modificado correctamente.",
                    content = @Content(schema = @Schema(implementation = HealthTipResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de actualización incorrectos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes autorización para modificar este consejo.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del consejo de salud especificado no existe.", content = @Content)
    })
    @PutMapping("/{healthId}")
    public ResponseEntity<HealthTipResponseDTO> updateHealthTip(
            @Parameter(description = "ID del consejo de salud a editar", required = true) @PathVariable Long healthId,
            @RequestBody HealthTipEntity healthTip) throws AccessDeniedException {

        return ResponseEntity.ok(healthTipService.update(healthId, healthTip));
    }

    @Operation(
            summary = "Eliminar un consejo de salud",
            description = "Da de baja y elimina de forma definitiva una recomendación médica del sistema utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Consejo de salud removido con éxito."),
            @ApiResponse(responseCode = "403", description = "Prohibido - No eres el autor o no posees los permisos para borrar este recurso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID provisto no coincide con ningún registro.", content = @Content)
    })
    @DeleteMapping("/{healthId}")
    public ResponseEntity<Void> deleteHealthTip(
            @Parameter(description = "ID del consejo de salud a eliminar", required = true) @PathVariable Long healthId) throws AccessDeniedException {
        healthTipService.delete(healthId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener todos los consejos de salud",
            description = "Recupera la lista global de recomendaciones públicas disponibles en la plataforma."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catálogo de consejos recuperado exitosamente.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = HealthTipResponseDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<HealthTipResponseDTO>> getAllHealthTips() {
        return ResponseEntity.ok(healthTipService.getAll());
    }

    @Operation(
            summary = "Obtener consejos de un doctor específico",
            description = "Filtra y devuelve todos las recomendaciones de salud creadas por un doctor determinado mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de consejos del doctor obtenida con éxito.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = HealthTipResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "El ID del profesional no fue encontrado.", content = @Content)
    })
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<HealthTipResponseDTO>> getHealthTipsByDoctor(
            @Parameter(description = "ID único del doctor", required = true) @PathVariable Long doctorId) {
        return ResponseEntity.ok(healthTipService.getHealthTipsByDoctor(doctorId));
    }

    @Operation(
            summary = "Obtener mis propios consejos de salud",
            description = "Permite al doctor que se encuentra autenticado consultar el listado exclusivo de los consejos que ha publicado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tus consejos de salud fueron recuperados correctamente.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = HealthTipResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Sesión inválida.", content = @Content)
    })
    @GetMapping("/my-tips")
    public ResponseEntity<List<HealthTipResponseDTO>> getMyTips() {
        return ResponseEntity.ok(healthTipService.getMyTips());
    }

    /*
    public ResponseEntity<List<HealthTipResponseDTO>> getPrivateTipsForPatient(@PathVariable Long doctorId,
                                                                               Authentication authentication) {

        String patientEmail = authentication.getName();

        List<HealthTipEntity> privateTips = healthTipService.getVisibleHealthTipsForPatient(patientEmail, doctorId);

        List<HealthTipResponseDTO> response = privateTips.stream()
                .map(healthTipMapper::toHealthTipResponseDTO)
                .toList();

        return ResponseEntity.ok(response);
    }
     */
}
