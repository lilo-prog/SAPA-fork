package com.example.SAPA.service;

import com.example.SAPA.DTOs.QuestionnaireDTO;
import com.example.SAPA.Models.Questionnaire.QuestionnaireEntity;
import com.example.SAPA.Repositories.QuestionnaireRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionnaireService {
    public final QuestionnaireRepository questionnaireRepository;

    public QuestionnaireService(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    public QuestionnaireEntity createQuestionnaire(QuestionnaireDTO dto){
        QuestionnaireEntity questionnaire = new QuestionnaireEntity();

        questionnaire.setTitle(dto.getTitle());
        questionnaire.setDescription(dto.getDescription());
        questionnaire.setFrequency(dto.getFrequency());
        return questionnaireRepository.save(questionnaire);
    }

    public void updateFrequency(Long QuestionnaireId, String frequency){
        System.out.println("questionnaire: " + QuestionnaireId + "frequency: " + frequency);
    }

    public String getResponses(Long patientId){
        return "Respuestas del paciente" + patientId;
    }
}

