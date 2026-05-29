package com.example.SAPA.entities;

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
@Table(name = "questionnaire_assignments")
public class QuestionnaireAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    private QuestionnaireEntity questionnaire;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @Column(nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    @Column(nullable = false)
    private boolean active;

    @PrePersist
    public void prePersist() {
        this.assignedAt = LocalDateTime.now();
        this.active = true;
    }
}
