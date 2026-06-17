package com.example.SAPA.mappers;

import com.example.SAPA.DTOs.MedicalDTO;
import com.example.SAPA.Models.MedicalRecord.MedicalRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TreatmentMapper.class})
public interface MedicalRecordMapper {

    @Mapping(source = "id", target = "medicalRecordId")
    MedicalDTO.MedicalRecordResponse toMedicalRecordResponse(MedicalRecordEntity medicalRecord);
}
