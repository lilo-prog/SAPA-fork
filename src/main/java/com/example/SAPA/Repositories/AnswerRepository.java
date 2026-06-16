package com.example.SAPA.Repositories;

import com.example.SAPA.Models.Questionnaire.AnswerEntity;
import com.example.SAPA.Models.Questionnaire.QuestionnaireResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {

    List<AnswerEntity> findByResponse(QuestionnaireResponseEntity response);
}
