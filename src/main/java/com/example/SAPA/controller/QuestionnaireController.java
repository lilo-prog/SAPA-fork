package com.example.SAPA.controller;

import com.example.SAPA.DTOs.QuestionnaireDTO;
import com.example.SAPA.service.QuestionnaireService;
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
@RequestMapping("/questionnaires")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Cuestionarios", description = "Operaciones para que los profesionales creen, modifiquen, eliminen y consulten plantillas de cuestionarios clínicos")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    @Operation(
            summary = "Crear plantilla de cuestionario",
            description = "Permite a un profesional de la salud diseñar y dar de alta un nuevo cuestionario con sus respectivas preguntas y opciones."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuestionario creado exitosamente.",
                    content = @Content(schema = @Schema(implementation = QuestionnaireDTO.QuestionnaireResponse.class))),
            @ApiResponse(responseCode = "400", description = "Los datos de la solicitud o la estructura de las preguntas son inválidos.", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado - El token de sesión no es válido.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<QuestionnaireDTO.QuestionnaireResponse> createQuestionnaire(
            @RequestBody QuestionnaireDTO.CreateQuestionnaireRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionnaireService.createQuestionnaire(request));
    }

    @Operation(
            summary = "Actualizar un cuestionario",
            description = "Permite modificar el título, la descripción o el conjunto de preguntas de un cuestionario existente a través de su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuestionario actualizado correctamente.",
                    content = @Content(schema = @Schema(implementation = QuestionnaireDTO.QuestionnaireResponse.class))),
            @ApiResponse(responseCode = "400", description = "Formato de datos de actualización incorrecto.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido - No eres el autor de este cuestionario o no tienes privilegios de edición.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del cuestionario especificado no fue encontrado.", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<QuestionnaireDTO.QuestionnaireResponse> updateQuestionnaire(
            @Parameter(description = "ID único del cuestionario a modificar", required = true) @PathVariable Long id,
            @RequestBody QuestionnaireDTO.UpdateQuestionnaireRequest request) {

        return ResponseEntity.ok(questionnaireService.updateQuestionnaire(id, request));
    }

    @Operation(
            summary = "Eliminar un cuestionario",
            description = "Remueve de forma permanente una plantilla de cuestionario del sistema mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cuestionario eliminado con éxito."),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes autorización para eliminar este recurso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID proporcionado no coincide con ningún registro activo.", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionnaire(
            @Parameter(description = "ID del cuestionario a eliminar", required = true) @PathVariable Long id) {
        questionnaireService.deleteQuestionnaire(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar mis cuestionarios creados",
            description = "Recupera la lista de todas las plantillas de cuestionarios que han sido diseñadas por el profesional de la salud autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cuestionarios del usuario recuperada correctamente.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionnaireDTO.QuestionnaireResponse.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Sesión inválida o expirada.", content = @Content)
    })
    @GetMapping("/my-questionnaires")
    public ResponseEntity<List<QuestionnaireDTO.QuestionnaireResponse>> getMyQuestionnaires() {
        return ResponseEntity.ok(questionnaireService.getMyQuestionnaires());
    }
}
