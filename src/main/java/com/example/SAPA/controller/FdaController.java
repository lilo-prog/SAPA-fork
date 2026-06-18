package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Response.fda.FdaResponseDTO;
import com.example.SAPA.service.FdaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fda")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Integración FDA", description = "Consultas e integraciones con el servicio externo de la FDA para información de medicamentos")
public class FdaController {

    private final FdaService fdaService;

    @Operation(
            summary = "Buscar medicamento en la FDA",
            description = "Consulta la base de datos oficial de la FDA para obtener información detallada de un medicamento utilizando su nombre."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información del medicamento recuperada exitosamente.",
                    content = @Content(schema = @Schema(implementation = FdaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "El parámetro del nombre del medicamento está vacío o es inválido.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontraron registros en la FDA para el medicamento proporcionado.", content = @Content),
            @ApiResponse(responseCode = "502", description = "Error de comunicación con el servicio externo de la FDA.", content = @Content)
    })
    @GetMapping("/search-medication")
    public FdaResponseDTO search(
            @Parameter(description = "Nombre comercial del medicamento a buscar", required = true, example = "Ibuprofen")
            @RequestParam String medicationName){
        return fdaService.searchForMedicationByName(medicationName);
    }
}
