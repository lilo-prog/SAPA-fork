package com.example.SAPA.Models.Entities;

import com.example.SAPA.Models.SpecialityEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "doctor")
public class DoctorEntity {
    // Entidad doctor.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="doctor_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(length = 1000)
    private String bio;

    private String hospitalUrl;

    private String phoneNumber;

    @ManyToMany
    @JoinTable(
            name = "doctor_speciality",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "speciality_id")
    )
    private List<SpecialityEntity> specialities;
}
