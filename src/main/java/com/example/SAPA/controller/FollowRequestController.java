package com.example.SAPA.controller;

import com.example.SAPA.Models.FollowRequestEntity;
import com.example.SAPA.service.FollowRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/follow-requests")
@RequiredArgsConstructor
public class FollowRequestController {

    private final FollowRequestService followRequestService;

    @PostMapping("/send/{doctorId}")
    public ResponseEntity<FollowRequestEntity> create(@PathVariable Long doctorId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(followRequestService.create(doctorId));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<FollowRequestEntity> approve(@PathVariable Long id) {
        return ResponseEntity.ok(followRequestService.approve(id));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<FollowRequestEntity> reject(@PathVariable Long id) {
        return ResponseEntity.ok(followRequestService.reject(id));
    }

    @PatchMapping("/{id}/dissolve")
    public ResponseEntity<FollowRequestEntity> dissolve(@PathVariable Long id) {
        return ResponseEntity.ok(followRequestService.dissolve(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FollowRequestEntity>> getPendingRequests() {
        return ResponseEntity.ok(followRequestService.getPendingRequests());
    }

    @GetMapping("/sent")
    public ResponseEntity<List<FollowRequestEntity>> getSentRequests() {
        return ResponseEntity.ok(followRequestService.getSentRequests());
    }
}
