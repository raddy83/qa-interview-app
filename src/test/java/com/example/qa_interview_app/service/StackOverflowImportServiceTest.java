package com.example.qa_interview_app.service;

import com.example.qa_interview_app.dto.StackOverflowAnswerDto;
import com.example.qa_interview_app.dto.StackOverflowAnswerResponseDto;
import com.example.qa_interview_app.dto.StackOverflowQuestionDto;
import com.example.qa_interview_app.dto.StackOverflowResponseDto;
import com.example.qa_interview_app.model.Question;
import com.example.qa_interview_app.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StackOverflowImportServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private StackOverflowImportService service;

    @Test
    void shouldImportQuestionsWithRealAnswersFromStackOverflowApi() {
        ReflectionTestUtils.setField(service, "baseUrl", "https://api.stackexchange.com/2.3/questions");
        ReflectionTestUtils.setField(service, "questionAnswersUrl", "https://api.stackexchange.com/2.3/questions");
        ReflectionTestUtils.setField(service, "site", "stackoverflow");
        ReflectionTestUtils.setField(service, "pageSize", 20);
        ReflectionTestUtils.setField(service, "sort", "votes");
        ReflectionTestUtils.setField(service, "order", "desc");
        ReflectionTestUtils.setField(service, "answerFilter", "withbody");

        StackOverflowQuestionDto questionDto = new StackOverflowQuestionDto();
        questionDto.setQuestionId(100L);
        questionDto.setAcceptedAnswerId(200L);
        questionDto.setTitle("How do I compare strings in Java?");

        StackOverflowResponseDto questionsResponse = new StackOverflowResponseDto();
        questionsResponse.setItems(List.of(questionDto));

        StackOverflowAnswerDto correctAnswer = new StackOverflowAnswerDto();
        correctAnswer.setAnswerId(200L);
        correctAnswer.setAccepted(true);
        correctAnswer.setBody("<p>Use equals() to compare string values.</p>");

        StackOverflowAnswerDto wrongAnswer = new StackOverflowAnswerDto();
        wrongAnswer.setAnswerId(201L);
        wrongAnswer.setAccepted(false);
        wrongAnswer.setBody("<p>Use == for all string comparison.</p>");

        StackOverflowAnswerResponseDto answersResponse = new StackOverflowAnswerResponseDto();
        answersResponse.setItems(List.of(correctAnswer, wrongAnswer));

        when(restTemplate.getForObject(contains("/questions?"), eq(StackOverflowResponseDto.class)))
                .thenReturn(questionsResponse);

        when(restTemplate.getForObject(contains("/100/answers"), eq(StackOverflowAnswerResponseDto.class)))
                .thenReturn(answersResponse);

        int imported = service.importQuestions("java");

        assertThat(imported).isEqualTo(1);

        ArgumentCaptor<List<Question>> captor = ArgumentCaptor.forClass(List.class);

        verify(questionRepository).saveAll(captor.capture());

        List<Question> savedQuestions = captor.getValue();

        assertThat(savedQuestions).hasSize(1);
        assertThat(savedQuestions.get(0).getContent()).isEqualTo("How do I compare strings in Java?");
        assertThat(savedQuestions.get(0).getAnswers()).hasSize(2);
        assertThat(savedQuestions.get(0).getAnswers())
                .anyMatch(answer -> answer.getContent().contains("equals()") && answer.isCorrect());
    }
}