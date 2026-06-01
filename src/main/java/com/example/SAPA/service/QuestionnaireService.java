package com.example.SAPA.service;

import com.example.SAPA.DTOs.QuestionnaireDTO;
import com.example.SAPA.Repositories.QuestionnaireRepository;
import com.example.SAPA.entities.QuestionnaireEntity;
import com.example.SAPA.exceptions.EmptyCollectionException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionnaireService {
    public final QuestionnaireRepository questionnaireRepository;

    public QuestionnaireService(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }
    
    public boolean validateQuestionaries(){
        if(questionnaireRepository.count()==0) return false;
        return true;
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
    
    public QuestionnaireEntity getQuestionnaireById(Long QuestionnaireId) throws EmptyCollectionException,  EntityNotFoundException {
        if(validateQuestionaries()) throw new  EmptyCollectionException("No hay cuestionarios");
        return questionnaireRepository.findById(QuestionnaireId).orElseThrow( ()-> new EntityNotFoundException("No se encontro el cuestionario"));
    }
}

