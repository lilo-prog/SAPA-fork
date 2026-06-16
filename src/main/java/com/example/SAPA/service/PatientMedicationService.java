package com.example.SAPA.service;

import com.example.SAPA.DTOs.Request.AddMedicationRequestDTO;
import com.example.SAPA.DTOs.Request.UpdateMedicationRequestDTO;
import com.example.SAPA.DTOs.Response.MedicationDetailResponseDTO;
import com.example.SAPA.DTOs.Response.PatientMedicationResponseDTO;
import com.example.SAPA.DTOs.Response.fda.FdaResponseDTO;
import com.example.SAPA.DTOs.Response.fda.FdaResultDTO;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.MedicalRecord.MedicalRecordEntity;
import com.example.SAPA.Models.MedicalRecord.PatientMedicationEntity;
import com.example.SAPA.Repositories.MedicalRecordRepository;
import com.example.SAPA.Repositories.PatientMedicationRepository;
import com.example.SAPA.Repositories.PatientRepository;
import com.example.SAPA.mappers.PatientMedicationMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientMedicationService {

    private final PatientMedicationRepository medicationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final PatientMedicationMapper medicationMapper;
    private final FdaService fdaService;
    private final UserContextService userContext;


    private MedicalRecordEntity getOrCreateMedicalRecord(PatientEntity patient) {
        if (patient.getMedicalRecord() != null) {
            return patient.getMedicalRecord();
        }
        MedicalRecordEntity record = medicalRecordRepository.save(new MedicalRecordEntity());
        patient.setMedicalRecord(record);
        patientRepository.save(patient);
        return record;
    }


    public PatientMedicationResponseDTO addMedication(Long patientId, AddMedicationRequestDTO request) {
        userContext.getAuthenticatedDoctor();

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con id: " + patientId));

        MedicalRecordEntity record = getOrCreateMedicalRecord(patient);

        PatientMedicationEntity medication = PatientMedicationEntity.builder()
                .medicalRecord(record)
                .fdaDrugName(request.fdaDrugName())
                .notes(request.notes())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

        return medicationMapper.toResponse(medicationRepository.save(medication));
    }


    public PatientMedicationResponseDTO updateMedication(Long medicationId, UpdateMedicationRequestDTO request) {
        userContext.getAuthenticatedDoctor();

        PatientMedicationEntity medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new EntityNotFoundException("Medicación no encontrada con id: " + medicationId));

        medication.setNotes(request.notes());
        medication.setStartDate(request.startDate());
        medication.setEndDate(request.endDate());

        return medicationMapper.toResponse(medicationRepository.save(medication));
    }


    public void deleteMedication(Long medicationId) {
        userContext.getAuthenticatedDoctor();

        PatientMedicationEntity medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new EntityNotFoundException("Medicación no encontrada con id: " + medicationId));

        medicationRepository.delete(medication);
    }


    public List<MedicationDetailResponseDTO> getMedications(Long patientId) {
        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con id: " + patientId));

        if (patient.getMedicalRecord() == null) {
            return List.of();
        }

        return medicationRepository.findByMedicalRecord(patient.getMedicalRecord())
                .stream()
                .map(med -> {
                    FdaResultDTO fdaDetails = null;
                    FdaResponseDTO fdaResponse = fdaService.searchForMedicationByName(med.getFdaDrugName());

                    if (fdaResponse != null && fdaResponse.getResults() != null
                            && !fdaResponse.getResults().isEmpty()) {
                        fdaDetails = fdaResponse.getResults().get(0);
                    }
                    return new MedicationDetailResponseDTO(
                            med.getId(),
                            med.getNotes(),
                            med.getStartDate(),
                            med.getEndDate(),
                            med.getAddedAt(),
                            fdaDetails
                    );
                })
                .toList();
    }
}
