package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Request.UpdateDoctorRequestDTO;
import com.example.SAPA.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody UpdateDoctorRequestDTO request) {
        doctorService.updateDoctor(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }
}
