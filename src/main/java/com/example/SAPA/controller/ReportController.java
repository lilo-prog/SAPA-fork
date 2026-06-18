package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Request.CreateReportRequestDTO;
import com.example.SAPA.DTOs.Response.ReportResponseDTO;
import com.example.SAPA.service.ReportService;
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
@RequestMapping("/reports")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Operaciones para la creación, consulta y moderación de reportes o denuncias dentro del sistema")
public class ReportController {

    private final ReportService reportService;

    @Operation(
            summary = "Crear un nuevo reporte",
            description = "Permite a un usuario registrar un reporte, denuncia o incidencia."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente.",
                    content = @Content(schema = @Schema(implementation = ReportResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "El cuerpo de la solicitud tiene datos inválidos o incompletos.", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT ausente o inválido.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ReportResponseDTO> createReport(@RequestBody CreateReportRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.createReport(request));
    }

    @Operation(
            summary = "Obtener reportes pendientes de revisión",
            description = "Recupera la lista de todos los reportes que aún no han sido auditados o procesados por el equipo de administración o moderación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reportes no revisados obtenida con éxito.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReportResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Sesión inválida.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes los roles de administrador o moderador necesarios para auditar reportes.", content = @Content)
    })
    @GetMapping("/unreviewed")
    public ResponseEntity<List<ReportResponseDTO>> getUnreviewed() {
        return ResponseEntity.ok(reportService.getUnreviewed());
    }

    @Operation(
            summary = "Marcar un reporte como revisado",
            description = "Cambia el estado de un reporte específico, lo que indica que ya fue procesado por la administración."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte actualizado y cerrado de forma exitosa.",
                    content = @Content(schema = @Schema(implementation = ReportResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token de sesión inválido.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido - No posees privilegios de moderación para ejecutar esta acción.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del reporte especificado no existe en el sistema.", content = @Content)
    })
    @PatchMapping("/{reportId}/review")
    public ResponseEntity<ReportResponseDTO> markAsReviewed(
            @Parameter(description = "ID único del reporte que se desea marcar como revisado", required = true) @PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.markAsReviewed(reportId));
    }
}
