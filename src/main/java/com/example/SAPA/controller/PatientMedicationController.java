package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Request.AddMedicationRequestDTO;
import com.example.SAPA.DTOs.Request.UpdateMedicationRequestDTO;
import com.example.SAPA.DTOs.Response.MedicationDetailResponseDTO;
import com.example.SAPA.DTOs.Response.PatientMedicationResponseDTO;
import com.example.SAPA.service.PatientMedicationService;
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
@RequestMapping("/medications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Medicación de Pacientes", description = "Operaciones para gestionar el plan de medicamentos, dosis y tratamientos farmacológicos asignados a los pacientes")
public class PatientMedicationController {

    private final PatientMedicationService medicationService;

    @Operation(
            summary = "Asignar un medicamento al paciente",
            description = "Registra un nuevo medicamento dentro del esquema de tratamiento de un paciente específico utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medicamento añadido exitosamente al plan de tratamiento.",
                    content = @Content(schema = @Schema(implementation = PatientMedicationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Los datos del medicamento (dosis, intervalos, etc.) son inválidos.", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT ausente o inválido.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del paciente especificado no existe.", content = @Content)
    })
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<PatientMedicationResponseDTO> addMedication(
            @Parameter(description = "ID único del paciente", required = true) @PathVariable Long patientId,
            @RequestBody AddMedicationRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicationService.addMedication(patientId, request));
    }

    @Operation(
            summary = "Actualizar medicamento del plan",
            description = "Modifica las condiciones de una asignación de medicamento existente (cambio de dosis, frecuencia u horarios) mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan de medicación actualizado correctamente.",
                    content = @Content(schema = @Schema(implementation = PatientMedicationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de actualización incorrectos o inconsistentes.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes autorización para alterar el tratamiento de este paciente.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID de la asignación de medicamento no fue encontrado.", content = @Content)
    })
    @PutMapping("/{medicationId}")
    public ResponseEntity<PatientMedicationResponseDTO> updateMedication(
            @Parameter(description = "ID de la asignación del medicamento a modificar", required = true) @PathVariable Long medicationId,
            @RequestBody UpdateMedicationRequestDTO request) {
        return ResponseEntity.ok(medicationService.updateMedication(medicationId, request));
    }

    @Operation(
            summary = "Remover un medicamento del plan",
            description = "Elimina la asignación de un medicamento del registro del paciente utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medicamento removido con éxito del tratamiento."),
            @ApiResponse(responseCode = "403", description = "Prohibido - No posees los privilegios médicos para suspender este recurso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del medicamento no coincide con ningún registro activo.", content = @Content)
    })
    @DeleteMapping("/{medicationId}")
    public ResponseEntity<Void> deleteMedication(
            @Parameter(description = "ID de la asignación del medicamento a eliminar", required = true) @PathVariable Long medicationId) {
        medicationService.deleteMedication(medicationId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener el plan de medicamentos de un paciente",
            description = "Recupera la lista detallada de todos los medicamentos que un paciente tiene asignados actualmente mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de medicamentos recuperado con éxito.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MedicationDetailResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Sesión inválida.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del paciente no existe en el sistema.", content = @Content)
    })
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicationDetailResponseDTO>> getMedications(
            @Parameter(description = "ID del paciente a consultar", required = true) @PathVariable Long patientId) {
        return ResponseEntity.ok(medicationService.getMedications(patientId));
    }
}
