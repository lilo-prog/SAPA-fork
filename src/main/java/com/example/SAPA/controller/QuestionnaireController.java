package com.example.SAPA.controller;

import com.example.SAPA.DTOs.QuestionnaireDTO;
import com.example.SAPA.service.QuestionnaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questionnaires")
@RequiredArgsConstructor
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    @PostMapping
    public ResponseEntity<QuestionnaireDTO.QuestionnaireResponse> createQuestionnaire(
            @RequestBody QuestionnaireDTO.CreateQuestionnaireRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionnaireService.createQuestionnaire(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionnaireDTO.QuestionnaireResponse> updateQuestionnaire(@PathVariable Long id,
                                                                                      @RequestBody QuestionnaireDTO.UpdateQuestionnaireRequest request) {
        return ResponseEntity.ok(questionnaireService.updateQuestionnaire(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionnaire(@PathVariable Long id) {
        questionnaireService.deleteQuestionnaire(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-creation")
    public ResponseEntity<List<QuestionnaireDTO.QuestionnaireResponse>> getMyQuestionnaires() {
        return ResponseEntity.ok(questionnaireService.getMyQuestionnaires());
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<QuestionnaireDTO.AssignmentResponse> assignQuestionnaire(@PathVariable Long id,
                                                                                   @RequestBody QuestionnaireDTO.AssignQuestionnaireRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionnaireService.assignQuestionnaire(id, request));
    }

    @GetMapping("/patient/{patientId}/responses")
    public ResponseEntity<List<QuestionnaireDTO.QuestionnaireResponseDTO>> getPatientResponses(@PathVariable Long patientId) {
        return ResponseEntity.ok(questionnaireService.getPatientResponses(patientId));
    }

    @GetMapping("/{id}/responses")
    public ResponseEntity<List<QuestionnaireDTO.QuestionnaireResponseDTO>> getResponsesByQuestionnaire(@PathVariable Long id) {
        return ResponseEntity.ok(questionnaireService.getResponsesByQuestionnaire(id));
    }

    @GetMapping("/my-assignments")
    public ResponseEntity<List<QuestionnaireDTO.AssignmentResponse>> getMyAssignments() {
        return ResponseEntity.ok(questionnaireService.getMyAssignments());
    }

    @PostMapping("/assignments/{assignmentId}/submit")
    public ResponseEntity<QuestionnaireDTO.QuestionnaireResponseDTO> submitResponse(@PathVariable Long assignmentId,
                                                                                    @RequestBody QuestionnaireDTO.SubmitResponseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionnaireService.submitResponse(assignmentId, request));
    }
}
