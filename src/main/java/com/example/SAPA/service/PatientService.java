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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    //Atributos
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    //Constructor
    public PatientService(PatientRepository patientRepository, UserRepository userRepository){
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    //Metodos
    public void validatePatients() throws EmptyCollectionException {
        if(patientRepository.findAll().isEmpty()) throw new EmptyCollectionException("No hay pacientes registrados");
    }

    public void validatePatientId(Long id) throws EmptyCollectionException{
        validatePatients();
        if(id<0||id>patientRepository.count()) throw new IllegalArgumentException("ID invalido");
    }

    public List<PatientDTOResponse> getAllPatients() throws EmptyCollectionException{
        validatePatients();
        return patientRepository.findAll().stream().map(PatientMapper::fromEntity).toList();
    }

    public Optional<PatientDTOResponse> getPatientById(Long id) throws EmptyCollectionException {
        validatePatientId(id);
        return Optional.of(patientRepository.findById(id).map(PatientMapper::fromEntity).orElseThrow(()-> new EntityNotFoundException("Paciente no encontrado.")));
    }

    public String savePatient(PatientDTORequest patientDTORequest) throws EmptyCollectionException {
        UserEntity user = userRepository.findById(patientDTORequest.getUser_id())
                        .orElseThrow(() -> new EmptyCollectionException("Usuario no encontrado."));

        patientRepository.save(
                PatientMapper.toEntity(patientDTORequest, user));
        return "Paciente agregado correctamente!";
    }

    public String updatePatient(Long id,PatientEntity patientEntity) throws EmptyCollectionException {
        validatePatientId(id);
        patientRepository.save(patientEntity);
        return "Paciente actualizado correctamente!";
    }

}
