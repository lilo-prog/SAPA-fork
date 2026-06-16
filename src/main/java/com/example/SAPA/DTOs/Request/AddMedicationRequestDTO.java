package com.example.SAPA.DTOs.Request;

import java.time.LocalDate;

public record AddMedicationRequestDTO(
        String fdaDrugName,
        String notes,
        LocalDate startDate,
        LocalDate endDate
) {}
