package com.example.SAPA.DTOs.Request;

import com.example.SAPA.Models.Entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTORequest {
    private Long id;

    private UserEntity user;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String phoneNumber;

    private Long medical_record_id;
}
