package com.example.SAPA.controller;

import com.example.SAPA.DTOs.QuestionnaireDTO;
import com.example.SAPA.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/responses")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService responseService;

    @PostMapping("/assignments/{assignmentId}/submit")
    public ResponseEntity<QuestionnaireDTO.QuestionnaireResponseDTO> submitResponse(@PathVariable Long assignmentId,
                                                                                    @RequestBody QuestionnaireDTO.SubmitResponseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseService.submitResponse(assignmentId, request));
    }
}
