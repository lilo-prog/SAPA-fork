package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Response.HealthTipResponseDTO;
import com.example.SAPA.Models.HealthTipEntity;
import com.example.SAPA.service.HealthTipService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/health-tips")
public class HealthTipController {
    //Atributos.
    private final HealthTipService healthTipService;

    //Constructor
    public HealthTipController(HealthTipService healthTipService) {
        this.healthTipService = healthTipService;
    }

    //Metodos
    @PostMapping("/create")
    public ResponseEntity<HealthTipEntity> createHealthTip(@RequestBody HealthTipEntity healthTip,
                                                           Authentication authentication) {

        String doctorEmail = authentication.getName();

        return ResponseEntity.ok(healthTipService.createHealthTip(healthTip, doctorEmail));
    }

    @GetMapping("/doctor/{doctorId}")
    public List<HealthTipResponseDTO> getHealthTipByDoctor(@PathVariable Long doctorId) {
        return healthTipService.getHealthTipsByDoctor(doctorId);
    }

    @GetMapping
    public ResponseEntity<List<HealthTipResponseDTO>> getAllHealthTips() {
        return ResponseEntity.ok(healthTipService.getAll());
    }

    @GetMapping("/my-tips")
    public ResponseEntity<List<HealthTipResponseDTO>> getMyTips(Authentication authentication) {
        String doctorEmail = authentication.getName();
        List<HealthTipResponseDTO> myTips = healthTipService.getTipsByDoctorEmail(doctorEmail);
        return ResponseEntity.ok(myTips);
    }

    @DeleteMapping("/delete/{healthId}")
    public ResponseEntity<String> deleteHealthTip(@PathVariable Long healthId) {
        healthTipService.delete(healthId);
        return ResponseEntity.ok("Consejo de salud eliminado correctamente");
    }

    @PutMapping("/update/{healthId}")
    public ResponseEntity<HealthTipResponseDTO> updateHealthTip(@PathVariable Long healthId,
                                                                @RequestBody HealthTipEntity healthTip,
                                                                Authentication authentication) throws AccessDeniedException {

        String doctorEmail = authentication.getName();

        return ResponseEntity.ok(healthTipService.update(healthId, healthTip, doctorEmail));
    }

    /*
    public ResponseEntity<List<HealthTipResponseDTO>> getPrivateTipsForPatient(@PathVariable Long doctorId,
                                                                               Authentication authentication) {

        String patientEmail = authentication.getName();

        List<HealthTipEntity> privateTips = healthTipService.getVisibleHealthTipsForPatient(patientEmail, doctorId);

        List<HealthTipResponseDTO> response = privateTips.stream()
                .map(healthTipMapper::toHealthTipResponseDTO)
                .toList();

        return ResponseEntity.ok(response);
    }

     */
}
