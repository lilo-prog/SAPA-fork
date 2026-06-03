package com.example.SAPA.controller;

import com.example.SAPA.DTOs.FdaResponseDTO;
import com.example.SAPA.service.FdaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FdaController {
    //Atributos.
    private final FdaService fdaService;

    //Constructor.
    public FdaController(FdaService fdaService) {
        this.fdaService = fdaService;
    }

    //Métodos
    @GetMapping("/search-medication")
    public FdaResponseDTO search(@RequestParam String medicationName){
        return fdaService.searchForMedicationByName(medicationName);
    }
}
