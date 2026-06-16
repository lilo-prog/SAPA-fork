package com.example.SAPA.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientRequestDTO {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phoneNumber;
}
