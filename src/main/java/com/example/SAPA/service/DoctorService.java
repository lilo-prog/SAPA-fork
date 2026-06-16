package com.example.SAPA.service;

import com.example.SAPA.DTOs.Request.UpdateDoctorRequestDTO;
import com.example.SAPA.Models.Entities.DoctorEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Repositories.DoctorRepository;
import com.example.SAPA.Repositories.SpecialityRepository;
import com.example.SAPA.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final SpecialityRepository specialityRepository;


    @Transactional
    public void updateDoctor(String email, UpdateDoctorRequestDTO request) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));

        DoctorEntity doctor = doctorRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado."));

        if (request.getFirstName() != null && !request.getFirstName().isBlank())
            doctor.setFirstName(request.getFirstName());

        if (request.getLastName() != null && !request.getLastName().isBlank())
            doctor.setLastName(request.getLastName());

        if (request.getBio() != null)
            doctor.setBio(request.getBio());

        if (request.getHospitalUrl() != null)
            doctor.setHospitalUrl(request.getHospitalUrl());

        if (request.getPhoneNumber() != null)
            doctor.setPhoneNumber(request.getPhoneNumber());

        if (request.getLicenseNumber() != null && !request.getLicenseNumber().isBlank())
            doctor.setLicenseNumber(request.getLicenseNumber());

        if (request.getSpecialities() != null && !request.getSpecialities().isEmpty())
            doctor.setSpecialities(specialityRepository.findAllById(request.getSpecialities()));

        doctorRepository.save(doctor);
    }


    public String getHospitalUrl(Long doctorId) {
        DoctorEntity doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado con id: " + doctorId));

        if (doctor.getHospitalUrl() == null || doctor.getHospitalUrl().isBlank()) {
            throw new EntityNotFoundException("Este médico no tiene URL de hospital registrada");
        }

        return doctor.getHospitalUrl();
    }
}