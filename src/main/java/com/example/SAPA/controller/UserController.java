package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Request.DeleteAccountRequest;
import com.example.SAPA.DTOs.Response.UserResponseDTO;
import com.example.SAPA.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> findAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserResponseDTO>> findActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<UserResponseDTO>> findInactiveUsers() {
        return ResponseEntity.ok(userService.getInactiveUsers());
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        UserResponseDTO profile = userService.getMyProfile(email);
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody DeleteAccountRequest request,
                                           Authentication authentication) {
        String email = authentication.getName();
        userService.deleteUser(email, request.password());
        return ResponseEntity.noContent().build();
    }
}
