package com.example.SAPA.controller;

import com.example.SAPA.DTOs.ForumDto;
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
    public ResponseEntity<List<ForumDto.PostResponse>> getPostsByForum(@PathVariable Long forumId) {
        return ResponseEntity.ok(postService.getPostsByForum(forumId));
    }

    @GetMapping("/{forumId}/filter/")
    public ResponseEntity<List<ForumDto.PostResponse>> filterPosts(@PathVariable Long forumId, @RequestParam String title){
        return ResponseEntity.ok(postService.filterPosts(forumId, title));
    }

    @PostMapping("/{forumId}/create")
    public ResponseEntity<ForumDto.PostResponse> createPost(@PathVariable Long forumId,
                                                            @RequestBody ForumDto.CreatePostRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(forumId, request));
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<ForumDto.PostResponse> updatePost(@PathVariable Long postId,
                                                            @RequestBody ForumDto.UpdatePostRequest request) {

        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
