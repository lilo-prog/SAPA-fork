package com.example.SAPA.entities;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "doctors")
public class DoctorEntity {

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

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(length = 1000)
    private String bio;

    private String hospitalUrl;

    private String phoneNumber;
}
