package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Response.fda.FdaResponseDTO;
import com.example.SAPA.service.FdaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medications/search")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MedicationSearchController {

    private final FdaService fdaService;

    @GetMapping
    public ResponseEntity<FdaResponseDTO> searchMedication(@RequestParam String name) {
        FdaResponseDTO result = fdaService.searchForMedicationByName(name);

        if (result == null || result.getResults() == null || result.getResults().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }
}
