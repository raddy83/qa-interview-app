package com.example.qa_interview_app.service;

import com.example.qa_interview_app.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QuestionImportServiceTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void shouldImportQuestionsFromJsonOnApplicationStartup() {
        assertThat(questionRepository.findAll())
                .isNotEmpty();
    }
}