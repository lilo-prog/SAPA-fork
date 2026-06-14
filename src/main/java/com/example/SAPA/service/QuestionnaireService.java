package com.example.SAPA.service;

import com.example.SAPA.DTOs.QuestionnaireDTO;
import com.example.SAPA.Models.Entities.DoctorEntity;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Models.Questionnaire.*;
import com.example.SAPA.Repositories.*;
import com.example.SAPA.mappers.QuestionnaireMapper;
import com.example.SAPA.security.entities.CredentialEntity;
import com.example.SAPA.security.repositories.CredentialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionRepository questionRepository;
    private final QuestionnaireAssignmentRepository assignmentRepository;
    private final QuestionnaireResponseRepository responseRepository;
    private final AnswerRepository answerRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final CredentialRepository credentialRepository;
    private final QuestionnaireMapper questionnaireMapper;


    private UserEntity getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        CredentialEntity credential = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario autenticado no encontrado"));

        return credential.getUser();
    }

    private DoctorEntity getAuthenticatedDoctor() {
        return doctorRepository.findByUser(getAuthenticatedUser())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el médico autenticado"));
    }

    private PatientEntity getAuthenticatedPatient() {
        return patientRepository.findByUser(getAuthenticatedUser())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el paciente autenticado"));
    }

    private String resolveDoctorName(DoctorEntity doctor) {
        return "Dr. " + doctor.getFirstName() + " " + doctor.getLastName();
    }

    private String resolvePatientName(PatientEntity patient) {
        return patient.getFirstName() + " " + patient.getLastName();
    }


    @Transactional
    public QuestionnaireDTO.QuestionnaireResponse createQuestionnaire(QuestionnaireDTO.CreateQuestionnaireRequest request) {
        DoctorEntity doctor = getAuthenticatedDoctor();

        QuestionnaireEntity questionnaire = QuestionnaireEntity.builder()
                .doctor(doctor)
                .title(request.title())
                .description(request.description())
                .frequency(request.frequency())
                .build();

        QuestionnaireEntity saved = questionnaireRepository.save(questionnaire);

        List<QuestionEntity> questions = request.questions().stream()
                .map(q -> QuestionEntity.builder()
                        .questionnaire(saved)
                        .text(q.text())
                        .type(q.type())
                        .orderIndex(q.orderIndex())
                        .build())
                .toList();

        questionRepository.saveAll(questions);
        saved.setQuestions(questions);

        return questionnaireMapper.toQuestionnaireResponse(saved, resolveDoctorName(doctor));
    }

    public QuestionnaireDTO.QuestionnaireResponse updateQuestionnaire(Long questionnaireId, QuestionnaireDTO.UpdateQuestionnaireRequest request) {
        DoctorEntity doctor = getAuthenticatedDoctor();

        QuestionnaireEntity questionnaire = questionnaireRepository.findById(questionnaireId)
                .orElseThrow(() -> new EntityNotFoundException("Cuestionario no encontrado con id: " + questionnaireId));

        if (!questionnaire.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException("No tenés permiso para modificar este cuestionario");
        }

        questionnaire.setTitle(request.title());
        questionnaire.setDescription(request.description());
        questionnaire.setFrequency(request.frequency());

        QuestionnaireEntity updated = questionnaireRepository.save(questionnaire);

        return questionnaireMapper.toQuestionnaireResponse(updated, resolveDoctorName(doctor));
    }

    public void deleteQuestionnaire(Long questionnaireId) {
        DoctorEntity doctor = getAuthenticatedDoctor();

        QuestionnaireEntity questionnaire = questionnaireRepository.findById(questionnaireId)
                .orElseThrow(() -> new EntityNotFoundException("Cuestionario no encontrado con id: " + questionnaireId));

        if (!questionnaire.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException("No tenés permiso para eliminar este cuestionario");
        }

        questionnaireRepository.delete(questionnaire);
    }

    public QuestionnaireDTO.AssignmentResponse assignQuestionnaire(Long questionnaireId, QuestionnaireDTO.AssignQuestionnaireRequest request) {
        DoctorEntity doctor = getAuthenticatedDoctor();

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
        return questionnaireMapper.toAssignmentResponse(saved, resolvePatientName(patient));
    }

    public List<QuestionnaireDTO.QuestionnaireResponse> getMyQuestionnaires() {
        DoctorEntity doctor = getAuthenticatedDoctor();

        return questionnaireRepository.findByDoctor(doctor)
                .stream()
                .map(q -> questionnaireMapper.toQuestionnaireResponse(q, resolveDoctorName(doctor)))
                .toList();
    }

    public List<QuestionnaireDTO.QuestionnaireResponseDTO> getPatientResponses(Long patientId) {
        DoctorEntity doctor = getAuthenticatedDoctor();

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con id: " + patientId));

        return assignmentRepository.findByQuestionnaire(null).stream()
                .filter(a -> a.getPatient().getId().equals(patient.getId())
                        && a.getQuestionnaire().getDoctor().getId().equals(doctor.getId()))
                .flatMap(a -> responseRepository.findByAssignmentOrderByAnsweredAtDesc(a).stream())
                .map(questionnaireMapper::toResponseDTO)
                .toList();
    }

    public List<QuestionnaireDTO.QuestionnaireResponseDTO> getResponsesByQuestionnaire(Long questionnaireId) {
        DoctorEntity doctor = getAuthenticatedDoctor();

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

    public List<QuestionnaireDTO.AssignmentResponse> getMyAssignments() {
        PatientEntity patient = getAuthenticatedPatient();

        return assignmentRepository.findByPatientAndActiveTrue(patient)
                .stream()
                .map(a -> questionnaireMapper.toAssignmentResponse(a, resolvePatientName(patient)))
                .toList();
    }

    @Transactional
    public QuestionnaireDTO.QuestionnaireResponseDTO submitResponse(Long assignmentId, QuestionnaireDTO.SubmitResponseRequest request) {
        PatientEntity patient = getAuthenticatedPatient();

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

        List<AnswerEntity> answers = request.answers().stream()
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


