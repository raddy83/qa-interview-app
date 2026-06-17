package com.example.qa_interview_app.service;

import com.example.qa_interview_app.model.Answer;
import com.example.qa_interview_app.model.Question;
import com.example.qa_interview_app.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void shouldReturnTrueWhenAnswerIsCorrect() {
        Question question = new Question();
        question.setId(1L);

        Answer answer = new Answer();
        answer.setId(10L);
        answer.setCorrect(true);
        answer.setQuestion(question);

        question.setAnswers(List.of(answer));

        when(questionRepository.findById(1L))
                .thenReturn(Optional.of(question));

        boolean result = questionService.checkAnswer(1L, 10L);

        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenAnswerIsIncorrect() {
        Question question = new Question();
        question.setId(1L);

        Answer answer = new Answer();
        answer.setId(11L);
        answer.setCorrect(false);
        answer.setQuestion(question);

        question.setAnswers(List.of(answer));

        when(questionRepository.findById(1L))
                .thenReturn(Optional.of(question));

        boolean result = questionService.checkAnswer(1L, 11L);

        assertThat(result).isFalse();
    }
}