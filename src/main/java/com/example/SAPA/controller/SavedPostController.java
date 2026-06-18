package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Response.SavedPostResponseDTO;
import com.example.SAPA.service.SavedPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/savedposts")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SavedPostController {

    private final SavedPostService savedPostService;

    @GetMapping
    public ResponseEntity<List<SavedPostResponseDTO>> getSavedPosts() {
        return ResponseEntity.ok(savedPostService.getSavedPosts());
    }

    @PostMapping("/{postId}")
    public ResponseEntity<SavedPostResponseDTO> savePost(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPostService.savePost(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> unsavePost(@PathVariable Long postId) {
        savedPostService.unsavePost(postId);
        return ResponseEntity.noContent().build();
    }
}
