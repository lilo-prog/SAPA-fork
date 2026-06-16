package com.example.SAPA.mappers;

import com.example.SAPA.DTOs.MedicalDTO;
import com.example.SAPA.Models.MedicalRecord.MedicalRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    @Mapping(source = "id", target = "medicalRecordId")
    @Mapping(source = "treatements", target = "treatments")
    MedicalDTO.MedicalRecordResponse toMedicalRecordResponse(MedicalRecordEntity medicalRecord);
}
