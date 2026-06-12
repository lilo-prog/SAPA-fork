package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Response.fda.FdaResponseDTO;
import com.example.SAPA.service.FdaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fda")
public class FdaController {

    private final FdaService fdaService;

    public FdaController(FdaService fdaService) {
        this.fdaService = fdaService;
    }

    @GetMapping("/search-medication")
    public FdaResponseDTO search(@RequestParam String medicationName){
        return fdaService.searchForMedicationByName(medicationName);
    }
}
