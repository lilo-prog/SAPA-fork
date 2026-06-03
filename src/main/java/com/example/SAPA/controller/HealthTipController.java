package com.example.SAPA.controller;

import com.example.SAPA.Models.HealthTipEntity;
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
    public HealthTipEntity create(@RequestBody HealthTipEntity healthTip) {
        return healthTipService.create(healthTip);
    }

    @GetMapping
    public List<HealthTipEntity> getAll() {
        return healthTipService.getAll();
    }

    @GetMapping("/visible")
    public List<HealthTipEntity> getVisibleHealthTips(@RequestParam Long patientId, @RequestParam Long doctorId){
        return healthTipService.getVisibleHealthTips(patientId, doctorId);
    }

    @PutMapping("/{id}")
    public HealthTipEntity update(@PathVariable Long id, @RequestBody HealthTipEntity healthTip) {
        return healthTipService.update(id, healthTip);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        healthTipService.delete(id);
    }
}
