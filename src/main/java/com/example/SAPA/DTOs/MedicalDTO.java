package com.example.SAPA.DTOs;

import com.example.SAPA.enums.TimeLapse;

import java.util.List;

public class MedicalDTO {

    public record DurationRequest(
            int length,
            TimeLapse timeLapse
    ) {}

    public record DurationResponse(
            Long durationId,
            int length,
            TimeLapse timeLapse
    ) {}

    public record CreateTreatmentRequest(
            String name,
            String description,
            DurationRequest duration,
            DurationRequest frecuency
    ) {}

    public record UpdateTreatmentRequest(
            String name,
            String description,
            DurationRequest duration,
            DurationRequest frecuency
    ) {}

    public record TreatmentResponse(
            Long treatmentId,
            String name,
            String description,
            DurationResponse duration,
            DurationResponse frecuency
    ) {}


    public record MedicalRecordResponse(
            Long medicalRecordId,
            List<TreatmentResponse> treatments
    ) {}
}
