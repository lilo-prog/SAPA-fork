package com.example.SAPA.DTOs;

import com.example.SAPA.enums.DurationUnit;
import com.example.SAPA.enums.FrequencyUnit;

import java.util.List;

public class MedicalDTO {

    public record TreatmentDuration(
            int length,
            DurationUnit unit
    ) {}

    public record TreatmentFrequency(
            int length,
            FrequencyUnit unit
    ) {}

    public record TreatmentRequest(
            String name,
            String description,
            TreatmentDuration duration,
            TreatmentFrequency frequency
    ) {}

    public record TreatmentResponse(
            Long treatmentId,
            String name,
            String description,
            TreatmentDurationResponse duration,
            TreatmentFrequencyResponse frequency
    ) {}

    public record TreatmentDurationResponse(
            int length,
            DurationUnit unit
    ) {}

    public record TreatmentFrequencyResponse(
            int length,
            FrequencyUnit unit
    ) {}

    public record MedicalRecordResponse(
            Long medicalRecordId,
            List<TreatmentResponse> treatments
    ) {}
}
