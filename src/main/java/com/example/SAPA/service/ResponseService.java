package com.example.SAPA.service;

import com.example.SAPA.DTOs.QuestionnaireDTO;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Questionnaire.AnswerEntity;
import com.example.SAPA.Models.Questionnaire.QuestionEntity;
import com.example.SAPA.Models.Questionnaire.QuestionnaireAssignmentEntity;
import com.example.SAPA.Models.Questionnaire.QuestionnaireResponseEntity;
import com.example.SAPA.Repositories.*;
import com.example.SAPA.mappers.QuestionnaireMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final QuestionRepository questionRepository;
    private final QuestionnaireAssignmentRepository assignmentRepository;
    private final QuestionnaireResponseRepository responseRepository;
    private final AnswerRepository answerRepository;
    private final QuestionnaireMapper questionnaireMapper;
    private final UserContextService userContextService;

    @Transactional
    public QuestionnaireDTO.QuestionnaireResponseDTO submitResponse(Long assignmentId, QuestionnaireDTO.SubmitResponseRequest request) {
        PatientEntity patient = userContextService.getAuthenticatedPatient();

        QuestionnaireAssignmentEntity assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Asignación no encontrada con id: " + assignmentId));

        if (!assignment.getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("No tenés permiso para responder este cuestionario");
        }

        if (!assignment.isActive()) {
            throw new RuntimeException("Este cuestionario ya no está activo");
        }

        QuestionnaireResponseEntity response = QuestionnaireResponseEntity.builder()
                .assignment(assignment)
                .build();

        QuestionnaireResponseEntity savedResponse = responseRepository.save(response);

        List<AnswerEntity> answers = request.answers()
                .stream()
                .map(a -> {
                    QuestionEntity question = questionRepository.findById(a.questionId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Pregunta no encontrada con id: " + a.questionId()));

                    return AnswerEntity.builder()
                            .response(savedResponse)
                            .question(question)
                            .value(a.value())
                            .build();
                })

                .toList();

        answerRepository.saveAll(answers);
        savedResponse.setAnswers(answers);

        return questionnaireMapper.toResponseDTO(savedResponse);
    }
}
