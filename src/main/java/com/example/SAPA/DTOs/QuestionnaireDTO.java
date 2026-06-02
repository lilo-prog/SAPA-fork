package com.example.SAPA.DTOs;

import com.example.SAPA.Models.Questionnaire.QuestionnaireEntity;
import com.example.SAPA.enums.SendFrequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireDTO {
    private String title;
    private String description;
    private SendFrequency frequency;

    public QuestionnaireEntity toEntity(QuestionnaireDTO questionnaireDTO){
        QuestionnaireEntity questionnaireEntity = new QuestionnaireEntity();
        questionnaireEntity.setTitle(questionnaireDTO.getTitle());
        questionnaireEntity.setDescription(questionnaireDTO.getDescription());
        questionnaireEntity.setFrequency(questionnaireDTO.getFrequency());
        return questionnaireEntity;
    }
}
