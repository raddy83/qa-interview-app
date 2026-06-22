package com.example.qa_interview_app.service;

import com.example.qa_interview_app.dto.OpenTriviaQuestionDto;
import com.example.qa_interview_app.dto.OpenTriviaResponseDto;
import com.example.qa_interview_app.model.Answer;
import com.example.qa_interview_app.model.Question;
import com.example.qa_interview_app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenTriviaImportService {

    private final RestTemplate restTemplate;
    private final QuestionRepository questionRepository;

    @Value("${integration.open-trivia.base-url}")
    private String baseUrl;

    public int importQuestions(int amount) {
        String url =
                baseUrl
                        + "?amount="
                        + amount
                        + "&type=multiple";

        OpenTriviaResponseDto response =
                restTemplate.getForObject(url, OpenTriviaResponseDto.class);

        if (response == null || response.getResponseCode() != 0) {
            throw new RuntimeException("Failed to import questions from Open Trivia API");
        }

        List<Question> questions = response.getResults()
                .stream()
                .map(this::mapToQuestion)
                .toList();

        questionRepository.saveAll(questions);

        return questions.size();
    }

    private Question mapToQuestion(OpenTriviaQuestionDto dto) {
        Question question = new Question();
        question.setCategory("API: " + clean(dto.getCategory()));
        question.setDifficulty(clean(dto.getDifficulty()));
        question.setContent(clean(dto.getQuestion()));
        question.setExplanation("Question imported from Open Trivia DB API.");

        List<Answer> answers = new ArrayList<>();

        Answer correctAnswer = new Answer();
        correctAnswer.setContent(clean(dto.getCorrectAnswer()));
        correctAnswer.setCorrect(true);
        correctAnswer.setQuestion(question);
        answers.add(correctAnswer);

        dto.getIncorrectAnswers().forEach(incorrect -> {
            Answer answer = new Answer();
            answer.setContent(clean(incorrect));
            answer.setCorrect(false);
            answer.setQuestion(question);
            answers.add(answer);
        });

        Collections.shuffle(answers);
        question.setAnswers(answers);

        return question;
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&quot;", "\"")
                .replace("&#039;", "'")
                .replace("&amp;", "&")
                .replace("&eacute;", "é");
    }

}
