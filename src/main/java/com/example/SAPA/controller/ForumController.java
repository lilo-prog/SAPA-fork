package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Request.ForumRequestDTO;
import com.example.SAPA.DTOs.Response.ForumResponseDTO;
import com.example.SAPA.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forums")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;

    @GetMapping("/all")
    public ResponseEntity<List<ForumResponseDTO>> getAllForums() {
        return ResponseEntity.ok(forumService.getAllForums());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ForumResponseDTO>> getActiveForums() {
        return ResponseEntity.ok(forumService.getActiveForums());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<ForumResponseDTO>> getInactiveForums() {
        return ResponseEntity.ok(forumService.getInactiveForums());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ForumResponseDTO>> filterForums(@RequestParam String title) {
        return ResponseEntity.ok(forumService.filterForums(title));
    }

    @PostMapping
    public ResponseEntity<ForumResponseDTO> createForum(@RequestBody ForumRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(forumService.createForum(request));
    }

    @PutMapping("/{forumId}")
    public ResponseEntity<ForumResponseDTO> updateForum(@PathVariable Long forumId,
                                                              @RequestBody ForumRequestDTO request) {
        return ResponseEntity.ok(forumService.updateForum(forumId, request));
    }

    @DeleteMapping("/{forumId}")
    public ResponseEntity<String> deleteForum(@PathVariable Long forumId) {
        forumService.deleteForum(forumId);
        return ResponseEntity.ok("Cambio de estado exitoso. Ahora el foro se encuentra en estado inactivo.");
    }
}
