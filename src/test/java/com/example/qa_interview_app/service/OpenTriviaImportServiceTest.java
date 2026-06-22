package com.example.qa_interview_app.service;

import com.example.qa_interview_app.dto.OpenTriviaQuestionDto;
import com.example.qa_interview_app.dto.OpenTriviaResponseDto;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenTriviaImportServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private OpenTriviaImportService service;

    @Test
    void shouldImportQuestionsFromOpenTriviaApi() {
        ReflectionTestUtils.setField(service, "baseUrl", "https://opentdb.com/api.php");

        OpenTriviaQuestionDto questionDto = new OpenTriviaQuestionDto();
        questionDto.setCategory("Science: Computers");
        questionDto.setDifficulty("medium");
        questionDto.setQuestion("What does CPU stand for?");
        questionDto.setCorrectAnswer("Central Processing Unit");
        questionDto.setIncorrectAnswers(List.of(
                "Computer Personal Unit",
                "Central Process Utility",
                "Control Processing Unit"
        ));

        OpenTriviaResponseDto response = new OpenTriviaResponseDto();
        response.setResponseCode(0);
        response.setResults(List.of(questionDto));

        when(restTemplate.getForObject(anyString(), eq(OpenTriviaResponseDto.class)))
                .thenReturn(response);

        int imported = service.importQuestions(1);

        assertThat(imported).isEqualTo(1);

        ArgumentCaptor<List<Question>> captor = ArgumentCaptor.forClass(List.class);

        verify(questionRepository).saveAll(captor.capture());

        List<Question> savedQuestions = captor.getValue();

        assertThat(savedQuestions).hasSize(1);
        assertThat(savedQuestions.get(0).getContent()).isEqualTo("What does CPU stand for?");
        assertThat(savedQuestions.get(0).getAnswers()).hasSize(4);
        assertThat(savedQuestions.get(0).getAnswers())
                .anyMatch(answer -> answer.getContent().equals("Central Processing Unit") && answer.isCorrect());
    }
}