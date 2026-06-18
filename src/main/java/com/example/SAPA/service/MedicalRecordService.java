package com.example.SAPA.service;

import com.example.SAPA.DTOs.Response.MedicalRecordResponseDTO;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.MedicalRecord.MedicalRecordEntity;
import com.example.SAPA.Repositories.MedicalRecordRepository;
import com.example.SAPA.Repositories.PatientRepository;
import com.example.SAPA.mappers.MedicalRecordMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final UserContextService userContext;


    private MedicalRecordEntity getOrCreateMedicalRecord(PatientEntity patient) {
        if (patient.getMedicalRecord() != null) {
            return patient.getMedicalRecord();
        }

        MedicalRecordEntity record = new MedicalRecordEntity();
        MedicalRecordEntity saved = medicalRecordRepository.save(record);

        patient.setMedicalRecord(saved);
        patientRepository.save(patient);

        return saved;
    }

    public MedicalRecordResponseDTO getMyMedicalRecord() {
        PatientEntity patient = userContext.getAuthenticatedPatient();
        MedicalRecordEntity record = getOrCreateMedicalRecord(patient);
        return medicalRecordMapper.toMedicalRecordResponse(record);
    }

    public MedicalRecordResponseDTO getPatientMedicalRecord(Long patientId) {

        userContext.getAuthenticatedDoctor();

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con id: " + patientId));

        if (patient.getMedicalRecord() == null) {
            throw new EntityNotFoundException("El paciente aún no tiene ficha médica");
        }

        return medicalRecordMapper.toMedicalRecordResponse(patient.getMedicalRecord());
    }
}
