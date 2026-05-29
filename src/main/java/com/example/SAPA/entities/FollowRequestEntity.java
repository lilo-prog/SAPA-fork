package com.example.SAPA.entities;

import com.example.SAPA.enums.FollowRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "follow_requests")
public class FollowRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity doctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FollowRequestStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime respondedAt;

    @PrePersist
    public void prePersist() {
        this.requestedAt = LocalDateTime.now();
        this.status = FollowRequestStatus.PENDING;
    }
}
