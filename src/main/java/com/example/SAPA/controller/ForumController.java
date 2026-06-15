package com.example.SAPA.controller;

import com.example.SAPA.DTOs.ForumDto;
import com.example.SAPA.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forums")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;


    @GetMapping
    public ResponseEntity<List<ForumDto.ForumResponse>> getAllForums() {
        return ResponseEntity.ok(forumService.getAllForums());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ForumDto.ForumResponse>> filterForums(@RequestParam String title) {
        return ResponseEntity.ok(forumService.filterForums(title));
    }

    @PostMapping
    public ResponseEntity<ForumDto.ForumResponse> createForum(@RequestBody ForumDto.CreateForumRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(forumService.createForum(request));
    }

    @PutMapping("/{forumId}")
    public ResponseEntity<ForumDto.ForumResponse> updateForum(@PathVariable Long forumId,
                                                              @RequestBody ForumDto.UpdateForumRequest request) {
        return ResponseEntity.ok(forumService.updateForum(forumId, request));
    }

    @DeleteMapping("/{forumId}")
    public ResponseEntity<Void> deleteForum(@PathVariable Long forumId) {
        forumService.deleteForum(forumId);
        return ResponseEntity.noContent().build();
    }
}
