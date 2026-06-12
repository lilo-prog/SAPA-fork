package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Request.UpdatePatientRequestDTO;
import com.example.SAPA.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody UpdatePatientRequestDTO request) {
        patientService.updatePatient(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }
}
