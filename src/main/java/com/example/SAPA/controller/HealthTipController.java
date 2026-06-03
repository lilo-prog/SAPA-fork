package com.example.SAPA.controller;

import com.example.SAPA.Models.HealthTipEntity;
import com.example.SAPA.service.HealthTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/health-tips")
public class HealthTipController {

    @Autowired
    private HealthTipService healthTipService;

    @PostMapping
    public HealthTipEntity create(@RequestBody HealthTipEntity healthTip) {
        return healthTipService.create(healthTip);
    }

    @GetMapping
    public List<HealthTipEntity> getAll() {
        return healthTipService.getAll();
    }

    @PutMapping("/{id}")
    public HealthTipEntity update(
            @PathVariable Long id,
            @RequestBody HealthTipEntity healthTip) {

        return healthTipService.update(id, healthTip);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        healthTipService.delete(id);
    }

    @GetMapping("/can-view")
    public boolean canPatientViewHealthTips(@RequestParam Long patientId, @RequestParam Long doctorId) {
        return healthTipService.canPatientViewHealthTips(patientId, doctorId);
    }
}
