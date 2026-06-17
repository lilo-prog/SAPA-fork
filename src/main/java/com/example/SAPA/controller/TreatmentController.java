package com.example.SAPA.controller;

import com.example.SAPA.DTOs.MedicalDTO;
import com.example.SAPA.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treatments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TreatmentController {

    private final TreatmentService treatmentService;

    @PostMapping("/patient/{patientId}")
    public ResponseEntity<MedicalDTO.TreatmentResponse> createTreatment(@PathVariable Long patientId,
                                                                        @RequestBody MedicalDTO.TreatmentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(treatmentService.createTreatment(patientId, request));
    }

    @PutMapping("/{treatmentId}")
    public ResponseEntity<MedicalDTO.TreatmentResponse> updateTreatment(@PathVariable Long treatmentId,
                                                                        @RequestBody MedicalDTO.TreatmentRequest request) {

        return ResponseEntity.ok(treatmentService.updateTreatment(treatmentId, request));
    }

    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Long treatmentId) {
        treatmentService.deleteTreatment(treatmentId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalDTO.TreatmentResponse>> getTreatments(@PathVariable Long patientId) {
        return ResponseEntity.ok(treatmentService.getTreatments(patientId));
    }


    @GetMapping("/patient/{patientId}/filter")
    public ResponseEntity<List<MedicalDTO.TreatmentResponse>> filterTreatments(@PathVariable Long patientId,
                                                                               @RequestParam String name) {
        return ResponseEntity.ok(treatmentService.filterTreatments(patientId, name));
    }
}
