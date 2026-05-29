package com.example.SAPA.entities;

import com.example.SAPA.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "questions")
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    private QuestionnaireEntity questionnaire;

    @Column(nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @Column(nullable = false)
    private Integer orderIndex;
}
