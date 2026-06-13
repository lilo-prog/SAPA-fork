package com.example.SAPA.Models.MedicalRecord;

import com.example.SAPA.DTOs.FdaResponseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "medicalRecord")
public class MedicalRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="medical_record_id")
    private Long id;
    private List<FdaResponseDTO> medications;
    private List<TreatmentEntity> treatements;

}
