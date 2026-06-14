package com.example.SAPA.DTOs;

import com.example.SAPA.enums.QuestionType;
import com.example.SAPA.enums.SendFrequency;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionnaireDTO {

    public record CreateQuestionnaireRequest(
            String title,
            String description,
            SendFrequency frequency,
            List<CreateQuestionRequest> questions
    ) {}

    public record CreateQuestionRequest(
            String text,
            QuestionType type,
            Integer orderIndex
    ) {}

    public record AssignQuestionnaireRequest(
            Long patientId
    ) {}

    public record UpdateQuestionnaireRequest(
            String title,
            String description,
            SendFrequency frequency
    ) {}

    public record SubmitResponseRequest(
            List<SubmitAnswerRequest> answers
    ) {}

    public record SubmitAnswerRequest(
            Long questionId,
            String value
    ) {}

    public record QuestionnaireResponse(
            Long questionnaireId,
            String doctorName,
            String title,
            String description,
            SendFrequency frequency,
            List<QuestionResponse> questions,
            LocalDateTime createdAt
    ) {}

    public record QuestionResponse(
            Long questionId,
            String text,
            QuestionType type,
            Integer orderIndex
    ) {}

    // Asignación de cuestionario a paciente
    public record AssignmentResponse(
            Long assignmentId,
            Long questionnaireId,
            String questionnaireTitle,
            String patientName,
            boolean active,
            LocalDateTime assignedAt
    ) {}

    // Respuesta completa de un paciente a un cuestionario
    public record QuestionnaireResponseDTO(
            Long responseId,
            Long assignmentId,
            List<AnswerResponse> answers,
            LocalDateTime answeredAt
    ) {}

    // Una respuesta individual
    public record AnswerResponse(
            Long answerId,
            Long questionId,
            String questionText,
            String value
    ) {}
}
