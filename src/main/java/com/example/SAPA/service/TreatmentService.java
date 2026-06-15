package com.example.SAPA.service;

import com.example.SAPA.DTOs.MedicalDTO;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Models.MedicalRecord.DurationEntity;
import com.example.SAPA.Models.MedicalRecord.MedicalRecordEntity;
import com.example.SAPA.Models.MedicalRecord.TreatmentEntity;
import com.example.SAPA.Repositories.DurationRepository;
import com.example.SAPA.Repositories.MedicalRecordRepository;
import com.example.SAPA.Repositories.PatientRepository;
import com.example.SAPA.Repositories.TreatmentRepository;
import com.example.SAPA.mappers.TreatmentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final DurationRepository durationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final UserContextService userContext;
    private final TreatmentMapper treatmentMapper;


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


    private DurationEntity buildDuration(MedicalDTO.DurationRequest request) {
        DurationEntity duration = new DurationEntity();
        duration.setLength(request.length());
        duration.setTimeLapse(request.timeLapse());
        return durationRepository.save(duration);
    }


    @Transactional
    public MedicalDTO.TreatmentResponse createTreatment(MedicalDTO.CreateTreatmentRequest request) {
        PatientEntity patient = userContext.getAuthenticatedPatient();
        MedicalRecordEntity record = getOrCreateMedicalRecord(patient);

        DurationEntity duration = buildDuration(request.duration());
        DurationEntity frecuency = buildDuration(request.frecuency());

        TreatmentEntity treatment = TreatmentEntity.builder()
                .name(request.name())
                .description(request.description())
                .duration(duration)
                .frecuency(frecuency)
                .medicalRecord(record)
                .build();

        TreatmentEntity saved = treatmentRepository.save(treatment);

        record.getTreatements().add(saved);
        medicalRecordRepository.save(record);

        return treatmentMapper.toTreatmentResponse(saved);
    }


    @Transactional
    public MedicalDTO.TreatmentResponse updateTreatment(Long treatmentId, MedicalDTO.UpdateTreatmentRequest request) {
        PatientEntity patient = userContext.getAuthenticatedPatient();

        TreatmentEntity treatment = treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new EntityNotFoundException("Tratamiento no encontrado con id: " + treatmentId));

        if (!treatment.getMedicalRecord().getId()
                .equals(patient.getMedicalRecord().getId())) {
            throw new RuntimeException("No tenés permiso para modificar este tratamiento");
        }

        treatment.getDuration().setLength(request.duration().length());
        treatment.getDuration().setTimeLapse(request.duration().timeLapse());
        durationRepository.save(treatment.getDuration());

        treatment.getFrecuency().setLength(request.frecuency().length());
        treatment.getFrecuency().setTimeLapse(request.frecuency().timeLapse());
        durationRepository.save(treatment.getFrecuency());

        treatment.setName(request.name());
        treatment.setDescription(request.description());

        return treatmentMapper.toTreatmentResponse(treatmentRepository.save(treatment));
    }


    public void deleteTreatment(Long treatmentId) {
        PatientEntity patient = userContext.getAuthenticatedPatient();

        TreatmentEntity treatment = treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new EntityNotFoundException("Tratamiento no encontrado con id: " + treatmentId));

        if (!treatment.getMedicalRecord().getId()
                .equals(patient.getMedicalRecord().getId())) {
            throw new RuntimeException("No tenés permiso para eliminar este tratamiento");
        }

        treatmentRepository.delete(treatment);
    }


    public List<MedicalDTO.TreatmentResponse> getTreatments(Long patientId) {
        UserEntity user = userContext.getAuthenticatedUser();

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con id: " + patientId));

        if (patient.getMedicalRecord() == null) {
            return List.of();
        }

        return treatmentRepository.findByMedicalRecord(patient.getMedicalRecord())
                .stream()
                .map(treatmentMapper::toTreatmentResponse)
                .toList();
    }

    public List<MedicalDTO.TreatmentResponse> filterTreatments(Long patientId, String name) {
        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con id: " + patientId));

        if (patient.getMedicalRecord() == null) {
            return List.of();
        }

        return treatmentRepository
                .findByMedicalRecordAndNameContainingIgnoreCase(patient.getMedicalRecord(), name)
                .stream()
                .map(treatmentMapper::toTreatmentResponse)
                .toList();
    }
}
