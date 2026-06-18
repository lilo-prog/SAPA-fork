package com.example.SAPA.controller;

import com.example.SAPA.Models.SpecialityEntity;
import com.example.SAPA.service.SpecialityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/specialities")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Especialidades Médicas", description = "Operaciones para consultar el catálogo de especialidades disponibles en el sistema")
public class SpecialityController {

    private final SpecialityService specialityService;

    @Operation(
            summary = "Obtener todas las especialidades",
            description = "Recupera la lista completa de especialidades médicas registradas en la plataforma (ej. Cardiología, Pediatría, Psicología)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catálogo de especialidades recuperado con éxito.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SpecialityEntity.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar el catálogo.", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<SpecialityEntity>> getAll() {
        return ResponseEntity.ok(specialityService.getAll());
    }
}
