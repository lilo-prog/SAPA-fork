package com.example.SAPA.service;

import com.example.SAPA.DTOs.Mappers.PatientMapper;
import com.example.SAPA.DTOs.Request.PatientDTORequest;
import com.example.SAPA.DTOs.Response.PatientDTOResponse;
import com.example.SAPA.DTOs.Mappers.UserMapper;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Repositories.PatientRepository;
import com.example.SAPA.Repositories.UserRepository;
import com.example.SAPA.exceptions.EmptyCollectionException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    //Atributos
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    //Metodos
    public void validatePatients() throws EmptyCollectionException {
        if(patientRepository.findAll().isEmpty()) throw new EmptyCollectionException("No hay pacientes registrados");
    }

    public List<PatientDTOResponse> getAllPatients() throws EmptyCollectionException{
        validatePatients();
        return patientRepository.findAll().stream().map(PatientMapper::fromEntity).toList();
    }

    public Optional<PatientDTOResponse> getPatientById(Long id) throws EmptyCollectionException {
        validatePatients();
        return Optional.of(patientRepository.findById(id).map(PatientMapper::fromEntity).orElseThrow(()-> new EntityNotFoundException("Paciente no encontrado.")));
    }

    public String savePatient(PatientDTORequest patientDTORequest) throws EmptyCollectionException {
        UserEntity user = userRepository.findById(patientDTORequest.getUser_id())
                        .orElseThrow(() -> new EmptyCollectionException("Usuario no encontrado."));

        patientRepository.save(
                PatientMapper.toEntity(patientDTORequest, user));
        return "Paciente agregado correctamente!";
    }

    public String updatePatient(PatientEntity patientEntity) throws EmptyCollectionException {
        validatePatients();
        patientRepository.save(patientEntity);
        return "Paciente actualizado correctamente!";
    }

}
