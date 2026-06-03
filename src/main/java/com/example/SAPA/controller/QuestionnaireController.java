package com.example.SAPA.controller;

import com.example.SAPA.DTOs.QuestionnaireDTO;
import com.example.SAPA.Models.Questionnaire.QuestionnaireEntity;
import com.example.SAPA.Models.Questionnaire.QuestionnaireResponseEntity;
import com.example.SAPA.enums.SendFrequency;
import com.example.SAPA.service.QuestionnaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("questionnaires")
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @PostMapping
    public QuestionnaireEntity createQuestionnaire(@RequestBody QuestionnaireDTO dto){

        return questionnaireService.createQuestionnaire(dto);
    }

    @PutMapping("/{id}/frequency")
    public ResponseEntity<String> updateFrequency(@PathVariable Long id, @RequestParam SendFrequency frequency){

        questionnaireService.updateFrequency(id, frequency);

        return ResponseEntity.ok("Frecuencia actualizada");
    }

    @GetMapping("/{patientId}/responses")
    public List<QuestionnaireResponseEntity> getResponses(@PathVariable Long patientId){

        return questionnaireService.getResponses(patientId);
    }
}
