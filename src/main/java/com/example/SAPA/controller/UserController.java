package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Request.DeleteAccountRequest;
import com.example.SAPA.DTOs.Request.RegisterRequest;
import com.example.SAPA.DTOs.Response.UserResponseDTO;
import com.example.SAPA.security.DTO.AuthResponse;
import com.example.SAPA.service.UserService;
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
    public List<UserResponseDTO> findAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/active")
    public List<UserResponseDTO> findActiveUsers() {
        return userService.getActiveUsers();
    }

    @GetMapping("/inactive")
    public List<UserResponseDTO> findInactiveUsers() {
        return userService.getInactiveUsers();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getMyProfile(Authentication authentication) {

        String email = authentication.getName();

        UserResponseDTO profile = userService.getMyProfile(email);

        return ResponseEntity.ok(profile);
    }

    @PatchMapping
    public ResponseEntity<String> deleteUser(@RequestBody DeleteAccountRequest request, Authentication authentication) {
        String email = authentication.getName();
        userService.deleteUser(email, request.password());
        return ResponseEntity.ok("Usuario dado de baja con éxito");
    }
}
