package com.example.SAPA.service;

import com.example.SAPA.DTOs.Request.UpdatePatientRequestDTO;
import com.example.SAPA.DTOs.Response.PatientResponseDTO;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Repositories.PatientRepository;
import com.example.SAPA.Repositories.UserRepository;
import com.example.SAPA.mappers.PatientMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;


    @Transactional
    public void updatePatient(String email, UpdatePatientRequestDTO request) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));

        PatientEntity patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado."));

        if (request.getFirstName() != null && !request.getFirstName().isBlank())
            patient.setFirstName(request.getFirstName());

        if (request.getLastName() != null && !request.getLastName().isBlank())
            patient.setLastName(request.getLastName());

        if (request.getBirthDate() != null)
            patient.setBirthDate(request.getBirthDate());

        if (request.getPhoneNumber() != null)
            patient.setPhoneNumber(request.getPhoneNumber());

        patientRepository.save(patient);
    }

    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toResponseDTO)
                .toList();
    }

    public PatientResponseDTO getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(patientMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado."));
    }

}
