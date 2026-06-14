package com.example.SAPA.Repositories;

import com.example.SAPA.Models.Questionnaire.QuestionnaireEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionnaireRepository extends JpaRepository<QuestionnaireEntity, Long> {
}
