package com.example.SAPA.mappers;

import com.example.SAPA.DTOs.MedicalDTO;
import com.example.SAPA.Models.MedicalRecord.TreatmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {

    @Mapping(source = "id", target = "treatmentId")
    MedicalDTO.TreatmentResponse toTreatmentResponse(TreatmentEntity treatment);
}
