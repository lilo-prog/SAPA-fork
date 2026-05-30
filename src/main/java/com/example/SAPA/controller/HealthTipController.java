package com.example.SAPA.controller;

import com.example.SAPA.entities.HealthTipEntity;
import com.example.SAPA.service.HealthTipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/health-tips")
public class HealthTipController {

    private final HealthTipService healthTipService;

    public HealthTipController(HealthTipService healthTipService) {
        this.healthTipService = healthTipService;
    }

    @PostMapping
    public HealthTipEntity createHealthTip(@RequestBody HealthTipEntity healthTip) {
        return healthTipService.createHealthTip(healthTip);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<HealthTipEntity> getHealthTipByDoctor(@PathVariable Long doctorId) {
        return healthTipService.getHealthTipsByDoctor(doctorId);
    }

    @GetMapping("/can-view")
    public boolean canPatientViewHealthTips(@RequestParam Long patientId, @RequestParam Long doctorId) {
        return healthTipService.canPatientViewHealthTips(patientId, doctorId);
    }
}
