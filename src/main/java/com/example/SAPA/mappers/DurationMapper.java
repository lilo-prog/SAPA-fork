package com.example.SAPA.mappers;

import com.example.SAPA.DTOs.MedicalDTO;
import com.example.SAPA.Models.MedicalRecord.DurationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DurationMapper {

    @Mapping(source = "id", target = "durationId")
    MedicalDTO.DurationResponse toDurationResponse(DurationEntity duration);
}
