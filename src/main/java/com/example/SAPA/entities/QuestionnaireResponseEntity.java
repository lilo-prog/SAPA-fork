package com.example.SAPA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "questionnaire_responses")

public class QuestionnaireResponseEntity {
    // Entidad de la respuesta total del cuestionario.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private QuestionnaireAssignmentEntity assignment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime answeredAt;

    @PrePersist
    public void prePersist() {
        this.answeredAt = LocalDateTime.now();
    }

}
