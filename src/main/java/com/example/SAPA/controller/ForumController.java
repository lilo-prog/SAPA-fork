package com.example.SAPA.controller;

import com.example.SAPA.DTOs.ForumDto;
import com.example.SAPA.service.ForumService;
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
@RequestMapping("/forums")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Foros de Discusión", description = "Operaciones para la creación, consulta, actualización y eliminación de foros comunitarios o médicos")
public class ForumController {

    private final ForumService forumService;

    @Operation(
            summary = "Listar todos los foros",
            description = "Recupera la lista completa de foros de discusión registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de foros obtenida con éxito.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ForumDto.ForumResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<ForumDto.ForumResponse>> getAllForums() {
        return ResponseEntity.ok(forumService.getAllForums());
    }

    @Operation(
            summary = "Filtrar foros por título",
            description = "Busca y devuelve los foros cuyos títulos coincidan con el texto proporcionado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados del filtro recuperados exitosamente.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ForumDto.ForumResponse.class)))),
            @ApiResponse(responseCode = "400", description = "El parámetro de búsqueda está vacío/es inválido.", content = @Content)
    })
    @GetMapping("/filter")
    public ResponseEntity<List<ForumDto.ForumResponse>> filterForums(
            @Parameter(description = "Texto o palabra clave contenida en el título del foro", required = true, example = "Salud Mental")
            @RequestParam String title) {
        return ResponseEntity.ok(forumService.filterForums(title));
    }

    @Operation(
            summary = "Crear un nuevo foro",
            description = "Registra un nuevo foro dentro de la plataforma."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Foro creado exitosamente.",
                    content = @Content(schema = @Schema(implementation = ForumDto.ForumResponse.class))),
            @ApiResponse(responseCode = "400", description = "El cuerpo de la solicitud cumple las validaciones de datos requeridas.", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado - Sesión inactiva/token inválido.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ForumDto.ForumResponse> createForum(@RequestBody ForumDto.CreateForumRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(forumService.createForum(request));
    }

    @Operation(
            summary = "Actualizar información de un foro",
            description = "Permite modificar datos esenciales como el título o la descripción de un foro existente mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foro actualizado correctamente.",
                    content = @Content(schema = @Schema(implementation = ForumDto.ForumResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes permisos para editar este foro.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El foro con el ID especificado no fue encontrado.", content = @Content)
    })
    @PutMapping("/{forumId}")
    public ResponseEntity<ForumDto.ForumResponse> updateForum(
            @Parameter(description = "ID único del foro", required = true) @PathVariable Long forumId,
            @RequestBody ForumDto.UpdateForumRequest request) {
        return ResponseEntity.ok(forumService.updateForum(forumId, request));
    }

    @Operation(
            summary = "Eliminar un foro",
            description = "Remueve de forma permanente un foro del sistema junto con sus datos asociados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Foro eliminado con éxito."),
            @ApiResponse(responseCode = "403", description = "Prohibido - No posees privilegios para remover este recurso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del foro no coincide con ningún registro activo.", content = @Content)
    })
    @DeleteMapping("/{forumId}")
    public ResponseEntity<Void> deleteForum(
            @Parameter(description = "ID del foro que se desea dar de baja", required = true) @PathVariable Long forumId) {
        forumService.deleteForum(forumId);
        return ResponseEntity.noContent().build();
    }
}
