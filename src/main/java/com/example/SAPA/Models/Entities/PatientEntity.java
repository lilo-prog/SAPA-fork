package com.example.SAPA.Models.Entities;


import com.example.SAPA.Models.MedicalRecord.MedicalRecordEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "patients")
public class PatientEntity {
    // Entidad paciente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private LocalDate birthDate;

    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name="medical_record_id")
    private MedicalRecordEntity medicalRecord;
}
