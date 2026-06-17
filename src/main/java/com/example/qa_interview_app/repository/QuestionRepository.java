package com.example.qa_interview_app.repository;

import com.example.qa_interview_app.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByCategory(String category);

    List<Question> findByDifficulty(String difficulty);

    List<Question> findByCategoryAndDifficulty(String category, String difficulty);

}
