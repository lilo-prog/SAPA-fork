package com.example.SAPA.service;

import com.example.SAPA.DTOs.QuestionnaireDTO;
import com.example.SAPA.Models.Entities.DoctorEntity;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Questionnaire.QuestionnaireAssignmentEntity;
import com.example.SAPA.Models.Questionnaire.QuestionnaireEntity;
import com.example.SAPA.Repositories.*;
import com.example.SAPA.mappers.QuestionnaireMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionnaireAssignmentRepository assignmentRepository;
    private final QuestionnaireResponseRepository responseRepository;
    private final PatientRepository patientRepository;
    private final QuestionnaireMapper questionnaireMapper;
    private final UserContextService userContextService;


    public QuestionnaireDTO.AssignmentResponse assignQuestionnaire(Long questionnaireId, QuestionnaireDTO.AssignQuestionnaireRequest request) {
        DoctorEntity doctor = userContextService.getAuthenticatedDoctor();

        QuestionnaireEntity questionnaire = questionnaireRepository.findById(questionnaireId)
                .orElseThrow(() -> new EntityNotFoundException("Cuestionario no encontrado con id: " + questionnaireId));

        if (!questionnaire.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException("No tenés permiso para asignar este cuestionario");
        }

        PatientEntity patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con id: " + request.patientId()));

        boolean alreadyAssigned = assignmentRepository
                .findByQuestionnaireAndPatientAndActiveTrue(questionnaire, patient)
                .isPresent();

        if (alreadyAssigned) {
            throw new RuntimeException("Este cuestionario ya está asignado activamente a este paciente");
        }

        QuestionnaireAssignmentEntity assignment = QuestionnaireAssignmentEntity.builder()
                .questionnaire(questionnaire)
                .patient(patient)
                .build();

        QuestionnaireAssignmentEntity saved = assignmentRepository.save(assignment);
        return questionnaireMapper.toAssignmentResponse(saved, userContextService.resolvePatientName(patient));
    }

    public List<QuestionnaireDTO.AssignmentResponse> getMyAssignments() {
        PatientEntity patient = userContextService.getAuthenticatedPatient();

        return assignmentRepository.findByPatientAndActiveTrue(patient)
                .stream()
                .map(a -> questionnaireMapper.toAssignmentResponse(a, userContextService.resolvePatientName(patient)))
                .toList();
    }

    public List<QuestionnaireDTO.QuestionnaireResponseDTO> getResponsesByQuestionnaire(Long questionnaireId) {
        DoctorEntity doctor = userContextService.getAuthenticatedDoctor();

        QuestionnaireEntity questionnaire = questionnaireRepository.findById(questionnaireId)
                .orElseThrow(() -> new EntityNotFoundException("Cuestionario no encontrado con id: " + questionnaireId));

        if (!questionnaire.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException("No tenés permiso para ver las respuestas de este cuestionario");
        }

        return assignmentRepository.findByQuestionnaire(questionnaire)
                .stream()
                .flatMap(a -> responseRepository.findByAssignmentOrderByAnsweredAtDesc(a).stream())
                .map(questionnaireMapper::toResponseDTO)
                .toList();
    }

    public List<QuestionnaireDTO.QuestionnaireResponseDTO> getPatientResponses(Long patientId) {
        DoctorEntity doctor = userContextService.getAuthenticatedDoctor();

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con id: " + patientId));

        return assignmentRepository.findByQuestionnaire(null).stream()
                .filter(a -> a.getPatient().getId().equals(patient.getId())
                        && a.getQuestionnaire().getDoctor().getId().equals(doctor.getId()))
                .flatMap(a -> responseRepository.findByAssignmentOrderByAnsweredAtDesc(a).stream())
                .map(questionnaireMapper::toResponseDTO)
                .toList();
    }
}
