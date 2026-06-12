package com.example.SAPA.service;

import com.example.SAPA.DTOs.Response.HealthTipResponseDTO;
import com.example.SAPA.Models.Entities.DoctorEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Models.HealthTipEntity;
import com.example.SAPA.Repositories.DoctorRepository;
import com.example.SAPA.Repositories.HealthTipRepository;
import com.example.SAPA.exceptions.CredentialsNotFoundException;
import com.example.SAPA.mappers.HealthTipMapper;
import com.example.SAPA.security.entities.CredentialEntity;
import com.example.SAPA.security.repositories.CredentialRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class HealthTipService {

    private final HealthTipRepository healthTipRepository;
    private final CredentialRepository credentialRepository;
    private final DoctorRepository doctorRepository;
    private final HealthTipMapper healthTipMapper;

    public HealthTipService(HealthTipRepository healthTipRepository, CredentialRepository credentialRepository,
                            DoctorRepository doctorRepository, HealthTipMapper healthTipMapper) {
        this.healthTipRepository = healthTipRepository;
        this.credentialRepository = credentialRepository;
        this.doctorRepository = doctorRepository;
        this.healthTipMapper = healthTipMapper;
    }

    //Métodos.
    public HealthTipEntity createHealthTip(HealthTipEntity healthTip, String doctorEmail){

        CredentialEntity credentials = credentialRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new CredentialsNotFoundException("Credenciales no encontradas"));

        UserEntity user = credentials.getUser();

        DoctorEntity doctor = doctorRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el perfil de médico"));

        healthTip.setDoctor(doctor);

        return healthTipRepository.save(healthTip);
    }

    public List<HealthTipResponseDTO> getHealthTipsByDoctor(Long doctorId){

        List<HealthTipEntity> healthTipEntity = healthTipRepository.findByDoctorId(doctorId);

        return healthTipEntity.
                stream().
                map(healthTipMapper::toHealthTipResponseDTO).
                toList();
    }

    public List<HealthTipResponseDTO> getAll(){
        return healthTipRepository.findAll()
                .stream()
                .map(healthTipMapper::toHealthTipResponseDTO)
                .toList();
    }

    public HealthTipResponseDTO update(Long id, HealthTipEntity healthTip, String doctorEmail) throws AccessDeniedException {

        HealthTipEntity existing = healthTipRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Consejo no encontrado"));

        String ownerEmail = existing.getDoctor().getUser().getEmail();

        if(!ownerEmail.equalsIgnoreCase(doctorEmail)){
            throw new AccessDeniedException("Acceso denegado: No es posible modificar el consejo de salud perteneciente a otro médico");
        }

        existing.setTitle(healthTip.getTitle());
        existing.setContent(healthTip.getContent());

        healthTipRepository.save(existing);

        return healthTipMapper.toHealthTipResponseDTO(existing);
    }

    public void delete(Long id){
        healthTipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consejo no encontrado"));

        healthTipRepository.deleteById(id);
    }

    public List<HealthTipResponseDTO> getTipsByDoctorEmail(String doctorEmail) {

        return healthTipRepository.findByDoctorUserEmail(doctorEmail)
                .stream()
                .map(healthTipMapper::toHealthTipResponseDTO)
                .toList();
    }

    /*
    public boolean isPatientLinkedWithDoctor(String patientEmail, Long doctorId) {
        return followRequestRepository
                .findByPatientUserEmailAndDoctorIdAndStatus(patientEmail, doctorId, FollowRequestStatus.APPROVED)
                .isPresent();
    }

    public List<HealthTipEntity> getVisibleHealthTipsForPatient(String patientEmail, Long doctorId) {

        boolean isLinked = isPatientLinkedWithDoctor(patientEmail, doctorId);

        if (!isLinked) {
            return Collections.emptyList();
        }

        return healthTipRepository.findByDoctorId(doctorId);
    }
     */
}