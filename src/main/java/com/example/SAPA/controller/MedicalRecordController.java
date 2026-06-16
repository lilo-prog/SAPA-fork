package com.example.SAPA.controller;

import com.example.SAPA.DTOs.MedicalDTO;
import com.example.SAPA.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medical-records")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @GetMapping("/my")
    public ResponseEntity<MedicalDTO.MedicalRecordResponse> getMyMedicalRecord() {
        return ResponseEntity.ok(medicalRecordService.getMyMedicalRecord());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<MedicalDTO.MedicalRecordResponse> getPatientMedicalRecord(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getPatientMedicalRecord(patientId));
    }
}
