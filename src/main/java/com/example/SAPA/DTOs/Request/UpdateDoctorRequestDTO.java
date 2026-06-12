package com.example.SAPA.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDoctorRequestDTO {
    private String firstName;
    private String lastName;
    private String bio;
    private String hospitalUrl;
    private String phoneNumber;
    private String licenseNumber;
    private List<Long> specialities;
}