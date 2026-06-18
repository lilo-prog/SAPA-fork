package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Request.ForumRequestDTO;
import com.example.SAPA.DTOs.Response.PostResponseDTO;
import com.example.SAPA.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{forumId}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByForum(@PathVariable Long forumId) {
        return ResponseEntity.ok(postService.getPostsByForum(forumId));
    }

    @GetMapping("/{forumId}/filter/")
    public ResponseEntity<List<PostResponseDTO>> filterPosts(@PathVariable Long forumId, @RequestParam String title){
        return ResponseEntity.ok(postService.filterPosts(forumId, title));
    }

    @PostMapping("/{forumId}/create")
    public ResponseEntity<PostResponseDTO> createPost(@PathVariable Long forumId,
                                                            @RequestBody ForumRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(forumId, request));
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long postId,
                                                            @RequestBody ForumRequestDTO request) {

        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
