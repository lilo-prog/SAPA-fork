package com.example.SAPA.service;

import com.example.SAPA.DTOs.Request.UpdatePatientRequestDTO;
import com.example.SAPA.DTOs.Response.PatientResponseDTO;
import com.example.SAPA.Models.Entities.DoctorEntity;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Repositories.DoctorRepository;
import com.example.SAPA.Repositories.PatientRepository;
import com.example.SAPA.Repositories.UserRepository;
import com.example.SAPA.mappers.PatientMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;
    private final UserContextService userContextService;


    @Transactional
    public void updatePatient(String email, UpdatePatientRequestDTO request) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));

        PatientEntity patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado."));

        if (request.firstName() != null && !request.firstName().isBlank()) {
            patient.setFirstName(request.firstName());
        }

        if (request.lastName() != null && !request.lastName().isBlank()) {
            patient.setLastName(request.lastName());
        }

        if (request.birthDate() != null) {
            patient.setBirthDate(request.birthDate());
        }

        if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
            patient.setPhoneNumber(request.phoneNumber());
        }
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getAllPatientsOfDoctor(String doctorEmail) {
        DoctorEntity doctor = userContextService.getAuthenticatedDoctor();

        return patientRepository.findAllByDoctor(doctor)
                .stream()
                .map(patientMapper::toResponseDTO)
                .toList();
    }
}
