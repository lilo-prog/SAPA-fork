package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Request.UpdateDoctorRequestDTO;
import com.example.SAPA.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctors")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Doctores", description = "Operaciones relacionadas con el perfil del cuerpo médico y configuraciones institucionales")
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(
            summary = "Actualizar perfil del médico",
            description = "Modifica los datos personales o profesionales del médico autenticado en la sesión actual."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Perfil actualizado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Los datos del formulario de actualización son inválidos.", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado - Sesión o token JWT no válidos.", content = @Content)
    })
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateDoctorRequestDTO request) {
        doctorService.updateDoctor(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener URL del hospital del doctor",
            description = "Recupera el enlace del portal institucional o del servidor del hospital asociado a un médico específico mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL recuperada exitosamente.",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "El ID del doctor proporcionado no existe en el sistema.", content = @Content)
    })
    @GetMapping("/{doctorId}/hospital-url")
    public ResponseEntity<String> getHospitalUrl(
            @Parameter(description = "ID del doctor a consultar", required = true) @PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.getHospitalUrl(doctorId));
    }
}
