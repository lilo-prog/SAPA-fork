package com.example.SAPA.controller;

import com.example.SAPA.DTOs.DeleteAccountRequest;
import com.example.SAPA.DTOs.RegisterRequest;
import com.example.SAPA.DTOs.Response.UserResponseDTO;
import com.example.SAPA.security.DTO.AuthResponse;
import com.example.SAPA.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse authResponse = userService.registerUser(request);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<UserResponseDTO> findAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getMyProfile(Authentication authentication) {

        String email = authentication.getName();

        UserResponseDTO profile = userService.getMyProfile(email);

        return ResponseEntity.ok(profile);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestBody DeleteAccountRequest request, Authentication authentication) {
        String email = authentication.getName();
        userService.deleteUser(email, request.password());
        return ResponseEntity.ok("Usuario eliminado con éxito");
    }
}
