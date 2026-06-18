package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Request.UpdatePatientRequestDTO;
import com.example.SAPA.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Operaciones relacionadas con el perfil y la gestión de datos de los pacientes")
public class PatientController {

    private final PatientService patientService;

    @Operation(
            summary = "Actualizar perfil del paciente",
            description = "Modifica los datos personales o de contacto del paciente que se encuentra actualmente autenticado en la sesión."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Perfil de paciente actualizado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Los datos enviados en el formulario de actualización son inválidos.", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido, expirado o ausente.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró el registro del paciente asociado al usuario autenticado.", content = @Content)
    })
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdatePatientRequestDTO request) {
        patientService.updatePatient(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }
}
