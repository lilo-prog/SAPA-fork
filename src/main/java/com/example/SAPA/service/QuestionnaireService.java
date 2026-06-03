package com.example.SAPA.service;

import com.example.SAPA.DTOs.QuestionnaireDTO;
import com.example.SAPA.Models.Entities.DoctorEntity;
import com.example.SAPA.Models.Questionnaire.QuestionEntity;
import com.example.SAPA.Models.Questionnaire.QuestionnaireEntity;
import com.example.SAPA.Models.Questionnaire.QuestionnaireResponseEntity;
import com.example.SAPA.Repositories.DoctorRepository;
import com.example.SAPA.Repositories.QuestionnaireRepository;
import com.example.SAPA.Repositories.QuestionnaireResponseRepository;
import com.example.SAPA.enums.SendFrequency;
import com.example.SAPA.security.entities.CredentialEntity;
import com.example.SAPA.security.repositories.CredentialRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionnaireService {
    public final QuestionnaireRepository questionnaireRepository;
    private final DoctorRepository doctorRepository;
    private final QuestionnaireResponseRepository responseRepository;
    private final CredentialRepository credentialRepository;

    public QuestionnaireService(QuestionnaireRepository questionnaireRepository, DoctorRepository doctorRepository, QuestionnaireResponseRepository responseRepository, CredentialRepository credentialRepository) {
        this.questionnaireRepository = questionnaireRepository;
        this.doctorRepository = doctorRepository;
        this.responseRepository = responseRepository;
        this.credentialRepository = credentialRepository;
    }

    public QuestionnaireEntity createQuestionnaire(QuestionnaireDTO dto){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        CredentialEntity credential = credentialRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Credential not found"));

        DoctorEntity doctor =
                doctorRepository.findByUserId(
                        credential.getUser().getId()
                ).orElseThrow(() ->
                        new RuntimeException("Doctor no encontrado"));

        QuestionnaireEntity questionnaire =
                new QuestionnaireEntity();

        questionnaire.setDoctor(doctor);
        questionnaire.setTitle(dto.getTitle());
        questionnaire.setDescription(dto.getDescription());
        questionnaire.setFrequency(dto.getFrequency());

        List<QuestionEntity> questions =
                dto.getQuestions()
                        .stream()
                        .map(questionDTO -> {

                            QuestionEntity question =
                                    new QuestionEntity();

                            question.setText(
                                    questionDTO.getText());

                            question.setType(
                                    questionDTO.getType());

                            question.setOrderIndex(
                                    questionDTO.getOrdexIndex());

                            question.setQuestionnaire(
                                    questionnaire);

                            return question;
                        })
                        .toList();

        questionnaire.setQuestions(questions);

        return questionnaireRepository.save(questionnaire);
    }

    public void updateFrequency(Long questionnaireId, SendFrequency frequency){
        QuestionnaireEntity questionnaire =
                questionnaireRepository.findById(questionnaireId).orElseThrow(() -> new RuntimeException("Questionnaire no encontrado"));
        questionnaire.setFrequency(frequency);

        questionnaireRepository.save(questionnaire);
    }

    public List<QuestionnaireResponseEntity> getResponses (Long patientId){
        return responseRepository.findByAssignmentPatientId(patientId);

    }
}

