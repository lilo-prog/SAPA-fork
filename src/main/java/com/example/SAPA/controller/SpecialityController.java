package com.example.SAPA.controller;

import com.example.SAPA.Models.SpecialityEntity;
import com.example.SAPA.service.SpecialityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/specialities")
public class SpecialityController {

    private final SpecialityService specialityService;

    public SpecialityController(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @GetMapping
    public ResponseEntity<List<SpecialityEntity>> getAll() {
        return ResponseEntity.ok(specialityService.getAll());
    }
}
