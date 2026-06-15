package com.example.SAPA.Models.Entities;

import com.example.SAPA.Models.MedicalRecord.MedicalRecordEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "patient")
public class PatientEntity {
    // Entidad paciente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="patient_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Past(message = "La fecha de nacimiento debe ser una fecha pasada (no puede ser hoy ni el futuro).")
    @NotNull
    private LocalDate birthDate;

    @Size(min=8, max=15)
    @NotNull
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecordEntity medicalRecord;
}
