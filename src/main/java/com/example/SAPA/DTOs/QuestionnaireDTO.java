package com.example.SAPA.DTOs;

import com.example.SAPA.enums.SendFrequency;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionnaireDTO {
    private String title;
    private String description;
    private SendFrequency frequency;
}
