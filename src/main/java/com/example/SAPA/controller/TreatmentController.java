package com.example.SAPA.controller;

import com.example.SAPA.DTOs.MedicalDTO;
import com.example.SAPA.service.TreatmentService;
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
@RequestMapping("/treatments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Tratamientos Médicos", description = "Operaciones para la creación, consulta, modificación y suspensión de tratamientos asignados a los pacientes")
public class TreatmentController {

    private final TreatmentService treatmentService;

    @Operation(
            summary = "Crear un nuevo tratamiento",
            description = "Registra un plan de tratamiento médico para un paciente específico utilizando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tratamiento médico creado exitosamente.",
                    content = @Content(schema = @Schema(implementation = MedicalDTO.TreatmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Los datos del tratamiento (fechas, indicaciones) son inválidos.", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o ausente.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes un vínculo activo para asignar tratamientos a este paciente.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del paciente especificado no existe.", content = @Content)
    })
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<MedicalDTO.TreatmentResponse> createTreatment(
            @Parameter(description = "ID único del paciente", required = true) @PathVariable Long patientId,
            @RequestBody MedicalDTO.TreatmentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(treatmentService.createTreatment(patientId, request));
    }

    @Operation(
            summary = "Actualizar un tratamiento existente",
            description = "Modifica los parámetros o instrucciones de un tratamiento asignado mediante su ID único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tratamiento actualizado correctamente.",
                    content = @Content(schema = @Schema(implementation = MedicalDTO.TreatmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de actualización erróneos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido - No posees autorización para modificar este tratamiento.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del tratamiento no fue encontrado.", content = @Content)
    })
    @PutMapping("/{treatmentId}")
    public ResponseEntity<MedicalDTO.TreatmentResponse> updateTreatment(
            @Parameter(description = "ID único del tratamiento a modificar", required = true) @PathVariable Long treatmentId,
            @RequestBody MedicalDTO.TreatmentRequest request) {

        return ResponseEntity.ok(treatmentService.updateTreatment(treatmentId, request));
    }

    @Operation(
            summary = "Eliminar o suspender un tratamiento",
            description = "Remueve de forma permanente un registro de tratamiento del historial del paciente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tratamiento eliminado exitosamente."),
            @ApiResponse(responseCode = "403", description = "Prohibido - No posees privilegios médicos para suspender este recurso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del tratamiento no coincide con ningún registro activo.", content = @Content)
    })
    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<Void> deleteTreatment(
            @Parameter(description = "ID del tratamiento que se desea dar de baja", required = true) @PathVariable Long treatmentId) {
        treatmentService.deleteTreatment(treatmentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener tratamientos de un paciente",
            description = "Recupera la lista histórica de todos los tratamientos médicos asignados a un paciente mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tratamientos recuperada con éxito.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MedicalDTO.TreatmentResponse.class)))),
            @ApiResponse(responseCode = "404", description = "El ID del paciente no existe en el sistema.", content = @Content)
    })
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalDTO.TreatmentResponse>> getTreatments(
            @Parameter(description = "ID del paciente a consultar", required = true) @PathVariable Long patientId) {
        return ResponseEntity.ok(treatmentService.getTreatments(patientId));
    }

    @Operation(
            summary = "Filtrar tratamientos de un paciente por nombre",
            description = "Busca tratamientos dentro del historial de un paciente cuyo tratamiento coincida con el parámetro enviado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados del filtro obtenidos exitosamente.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MedicalDTO.TreatmentResponse.class)))),
            @ApiResponse(responseCode = "404", description = "El paciente especificado no existe.", content = @Content)
    })
    @GetMapping("/patient/{patientId}/filter")
    public ResponseEntity<List<MedicalDTO.TreatmentResponse>> filterTreatments(
            @Parameter(description = "ID del paciente", required = true) @PathVariable Long patientId,
            @Parameter(description = "Texto o palabra clave del tratamiento (ej. Tratamiento Psicológico)", required = true, example = "Kinesiología") @RequestParam String name) {
        return ResponseEntity.ok(treatmentService.filterTreatments(patientId, name));
    }
}
