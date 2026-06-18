package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Request.UpdatePatientRequestDTO;
import com.example.SAPA.DTOs.Response.PatientResponseDTO;
import com.example.SAPA.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody UpdatePatientRequestDTO request) {
        patientService.updatePatient(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-patients")
    public ResponseEntity<List<PatientResponseDTO>> getMyPatients(Authentication authentication) {
        String doctorEmail = authentication.getName();
        return ResponseEntity.ok(patientService.getAllPatientsOfDoctor(doctorEmail));
    }
}
