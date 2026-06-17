package com.example.SAPA.service;

import com.example.SAPA.DTOs.MedicalDTO;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Models.MedicalRecord.DurationEntity;
import com.example.SAPA.Models.MedicalRecord.MedicalRecordEntity;
import com.example.SAPA.Models.MedicalRecord.TreatmentEntity;
import com.example.SAPA.Repositories.*;
import com.example.SAPA.enums.UserCategory;
import com.example.SAPA.mappers.TreatmentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
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
    private final FollowRequestRepository followRequestRepository;


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
        UserEntity currentUser = userContext.getAuthenticatedUser();

        // 1. Validar el acceso antes de continuar
        validarAccesoAPaciente(currentUser, patientId);

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
        UserEntity currentUser = userContext.getAuthenticatedUser();

        // 1. Validar el acceso antes de continuar
        validarAccesoAPaciente(currentUser, patientId);

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
    private void validarAccesoAPaciente(UserEntity currentUser, Long patientId) {
        // REGLA 1: Si el usuario autenticado es un PACIENTE
        if (currentUser.getRole().equals(UserCategory.PATIENT)) { // Adapta esto a cómo manejes tus Roles/Enums
            // Verificamos que el ID del paciente consultado coincida con su propio ID de usuario/paciente
            if (!currentUser.getId().equals(patientId)) {
                throw new IllegalArgumentException("No tienes permiso para ver la ficha médica de otro paciente.");
            }
            return; // Acceso permitido
        }

        // REGLA 2: Si el usuario autenticado es un MÉDICO
        if (currentUser.getRole().equals(UserCategory.DOCTOR)) {
            // Buscamos en la base de datos si existe una relación activa de seguimiento entre este médico y el paciente
            // Nota: Adapta este método según cómo se llame tu repositorio y tu entidad de seguimientos
            boolean tieneSeguimiento = followRequestRepository.existsByMedicoIdAndPacienteId(currentUser.getId(), patientId);

            if (!tieneSeguimiento) {
                throw new IllegalArgumentException("No tienes permiso para acceder a este paciente porque no está bajo tu seguimiento.");
            }
            return; // Acceso permitido
        }

        // Si cae aquí (por ejemplo, es otro rol no contemplado o no tiene rol válido)
        throw new IllegalArgumentException("Acceso denegado: Rol no autorizado para realizar esta consulta.");
    }
}
