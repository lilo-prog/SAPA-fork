package com.example.SAPA.DTOs.Mappers;

import com.example.SAPA.DTOs.Request.PatientDTORequest;
import com.example.SAPA.DTOs.Request.UserDTORequest;
import com.example.SAPA.DTOs.Response.PatientDTOResponse;
import com.example.SAPA.DTOs.Response.UserDTOResponse;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.enums.AccountStatus;
import com.example.SAPA.enums.UserCategory;
import com.example.SAPA.exceptions.EmptyCollectionException;
import com.example.SAPA.service.UserService;

import java.util.Optional;

public class PatientMapper {
    private static UserService userService;

    public PatientMapper(UserService userService) {
        this.userService = userService;
    }

    public static PatientEntity toEntity(PatientDTORequest patientDTORequest,UserEntity userEntity) throws EmptyCollectionException {
        PatientEntity patientEntity = new PatientEntity();

        patientEntity.setId(patientDTORequest.getId());
        patientEntity.setFirstName(patientDTORequest.getFirstName());
        patientEntity.setLastName(patientDTORequest.getLastName());
        patientEntity.setBirthDate(patientDTORequest.getBirthDate());
        patientEntity.setPhoneNumber(patientDTORequest.getPhoneNumber());

        // Seteas la relación del usuario que ya buscaste en la base de datos
        patientEntity.setUser(userEntity);
        return patientEntity;
    }

    public static PatientDTOResponse fromEntity(PatientEntity patientEntity){
        PatientDTOResponse patientDTOResponse = new PatientDTOResponse();
        patientDTOResponse.setFirstName(patientEntity.getFirstName());
        patientDTOResponse.setLastName(patientEntity.getLastName());
        patientDTOResponse.setUser_id(patientEntity.getUser().getId());
        patientDTOResponse.setMedical_record_id(patientEntity.getMedicalRecord().getId());
        return patientDTOResponse;
    }
}
