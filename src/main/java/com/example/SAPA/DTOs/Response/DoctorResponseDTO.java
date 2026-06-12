package com.example.SAPA.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponseDTO {

    private String firstName;
    private String lastName;
    private String licenseNumber;
}
