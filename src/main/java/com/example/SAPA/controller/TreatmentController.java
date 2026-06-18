package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Request.TreatmentRequestDTO;
import com.example.SAPA.DTOs.Response.TreatmentResponseDTO;
import com.example.SAPA.service.TreatmentService;
import jakarta.validation.Valid;
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
    public ResponseEntity<TreatmentResponseDTO> createTreatment(@PathVariable Long patientId,
                                                                @Valid @RequestBody TreatmentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(treatmentService.createTreatment(patientId, request));
    }

    @PutMapping("/{treatmentId}")
    public ResponseEntity<TreatmentResponseDTO> updateTreatment(@PathVariable Long treatmentId,
                                                                @Valid @RequestBody TreatmentRequestDTO request) {
        return ResponseEntity.ok(treatmentService.updateTreatment(treatmentId, request));
    }

    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Long treatmentId) {
        treatmentService.deleteTreatment(treatmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<TreatmentResponseDTO>> getTreatments(@PathVariable Long patientId) {
        return ResponseEntity.ok(treatmentService.getTreatments(patientId));
    }

    @GetMapping("/patient/{patientId}/filter")
    public ResponseEntity<List<TreatmentResponseDTO>> filterTreatments(@PathVariable Long patientId,
                                                                       @RequestParam String name) {
        return ResponseEntity.ok(treatmentService.filterTreatments(patientId, name));
    }
}
