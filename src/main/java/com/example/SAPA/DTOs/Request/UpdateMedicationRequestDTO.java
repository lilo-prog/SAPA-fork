package com.example.SAPA.DTOs.Request;

import java.time.LocalDate;

public record UpdateMedicationRequestDTO(
        String notes,
        LocalDate startDate,
        LocalDate endDate
) {}
