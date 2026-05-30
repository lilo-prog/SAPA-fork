package com.example.SAPA.controller;

import com.example.SAPA.DTOs.QuestionnaireDTO;
import com.example.SAPA.entities.QuestionnaireEntity;
import com.example.SAPA.service.QuestionnaireService;
import org.springframework.web.bind.annotation.*;

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
    public String updateFrequency(@PathVariable Long id, @RequestParam String frequency){
        questionnaireService.updateFrequency(id, frequency);

        return "Questionnaire updated";
    }

    @GetMapping("/{patientId}/responses")
    public String getResponses(@PathVariable Long patientId){
        return questionnaireService.getResponses(patientId);
    }


}
