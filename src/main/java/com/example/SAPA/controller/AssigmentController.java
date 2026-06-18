package com.example.SAPA.controller;

import com.example.SAPA.DTOs.QuestionnaireDTO;
import com.example.SAPA.service.AssignmentService;
import com.example.SAPA.service.ResponseService;
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
@RequestMapping("/assignments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Asignaciones y Respuestas", description = "Operaciones para asignar cuestionarios a pacientes y gestionar la entrega de sus respuestas")
public class AssigmentController {

    private final AssignmentService assignmentService;
    private final ResponseService responseService;

    @Operation(
            summary = "Asignar cuestionario a un paciente",
            description = "Asigna un cuestionario específico a uno o varios pacientes utilizando el ID del cuestionario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuestionario asignado exitosamente",
                    content = @Content(schema = @Schema(implementation = QuestionnaireDTO.AssignmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content),
            @ApiResponse(responseCode = "44", description = "Cuestionario o Paciente no encontrado", content = @Content)
    })
    @PostMapping("/{id}")
    public ResponseEntity<QuestionnaireDTO.AssignmentResponse> assignQuestionnaire(
            @Parameter(description = "ID del cuestionario a asignar", required = true) @PathVariable Long id,
            @RequestBody QuestionnaireDTO.AssignQuestionnaireRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assignmentService.assignQuestionnaire(id, request));
    }

    @Operation(
            summary = "Obtener asignaciones del usuario autenticado",
            description = "Devuelve la lista de cuestionarios asignados que pertenecen al paciente que ha iniciado sesión."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de asignaciones obtenida correctamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionnaireDTO.AssignmentResponse.class))))
    })
    @GetMapping("/my-assignments")
    public ResponseEntity<List<QuestionnaireDTO.AssignmentResponse>> getMyAssignments() {
        return ResponseEntity.ok(assignmentService.getMyAssignments());
    }

    @Operation(
            summary = "Obtener respuestas asociadas a un cuestionario",
            description = "Recupera el historial de respuestas completadas basándose en el ID de un cuestionario específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de respuestas recuperada con éxito",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionnaireDTO.QuestionnaireResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Cuestionario no encontrado", content = @Content)
    })
    @GetMapping("/{id}/responses")
    public ResponseEntity<List<QuestionnaireDTO.QuestionnaireResponseDTO>> getResponsesByQuestionnaire(
            @Parameter(description = "ID del cuestionario", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getResponsesByQuestionnaire(id));
    }

    @Operation(
            summary = "Obtener respuestas de un paciente específico",
            description = "Permite a un profesional de la salud consultar todas las respuestas cargadas por un paciente mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de respuestas del paciente obtenida con éxito",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionnaireDTO.QuestionnaireResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content)
    })
    @GetMapping("/patient/{patientId}/responses")
    public ResponseEntity<List<QuestionnaireDTO.QuestionnaireResponseDTO>> getPatientResponses(
            @Parameter(description = "ID del paciente", required = true) @PathVariable Long patientId) {
        return ResponseEntity.ok(assignmentService.getPatientResponses(patientId));
    }

    @Operation(
            summary = "Responder y finalizar un cuestionario asignado",
            description = "Registra las respuestas enviadas por el paciente para resolver la asignación pendiente del cuestionario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Respuestas guardadas y cuestionario completado exitosamente",
                    content = @Content(schema = @Schema(implementation = QuestionnaireDTO.QuestionnaireResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "El cuerpo de la respuesta tiene errores de validación", content = @Content),
            @ApiResponse(responseCode = "404", description = "La asignación no existe", content = @Content)
    })
    @PostMapping("/{assignmentId}/submit")
    public ResponseEntity<QuestionnaireDTO.QuestionnaireResponseDTO> submitResponse(
            @Parameter(description = "ID de la asignación del cuestionario", required = true) @PathVariable Long assignmentId,
            @RequestBody QuestionnaireDTO.SubmitResponseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseService.submitResponse(assignmentId, request));
    }
}
