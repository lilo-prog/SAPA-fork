package com.example.SAPA.DTOs.Response;

import com.example.SAPA.Models.MedicalRecord.MedicalRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTOResponse {
    private Long user_id;

    private String firstName;

    private String lastName;

    private Long medical_record_id;
}
