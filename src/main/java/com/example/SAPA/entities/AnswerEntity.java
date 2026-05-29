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
@Table(name = "answers")
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "response_id", nullable = false)
    private QuestionnaireResponseEntity response;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @Column(nullable = false, length = 2000)
    private String value;

}
