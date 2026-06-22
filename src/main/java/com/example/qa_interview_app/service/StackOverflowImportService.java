package com.example.qa_interview_app.service;

import com.example.qa_interview_app.dto.StackOverflowAnswerResponseDto;
import com.example.qa_interview_app.dto.StackOverflowQuestionDto;
import com.example.qa_interview_app.dto.StackOverflowResponseDto;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StackOverflowImportService {

    private final RestTemplate restTemplate;
    private final QuestionRepository questionRepository;

    @Value("${integration.stackoverflow.base-url}")
    private String baseUrl;

    @Value("${integration.stackoverflow.site}")
    private String site;

    @Value("${integration.stackoverflow.page-size}")
    private Integer pageSize;

    @Value("${integration.stackoverflow.sort}")
    private String sort;

    @Value("${integration.stackoverflow.order}")
    private String order;

    @Value("${integration.stackoverflow.answer-url}")
    private String answerUrl;

    @Value("${integration.stackoverflow.question-answers-url}")
    private String questionAnswersUrl;

    @Value("${integration.stackoverflow.answer-filter}")
    private String answerFilter;

    private List<Answer> fetchAnswersForQuestion(Question question, Long questionId, Long acceptedAnswerId) {
        String url = questionAnswersUrl
                + "/"
                + questionId
                + "/answers"
                + "?order=desc"
                + "&sort=votes"
                + "&site="
                + site
                + "&filter="
                + answerFilter;

        StackOverflowAnswerResponseDto response =
                restTemplate.getForObject(url, StackOverflowAnswerResponseDto.class);

        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
            return List.of();
        }

        List<Answer> answers = response.getItems()
                .stream()
                .limit(4)
                .map(dto -> {
                    Answer answer = new Answer();
                    answer.setContent(
                            shorten(
                                    removeHtml(
                                            dto.getBody()
                                    )
                            )
                    );
                    answer.setCorrect(
                            acceptedAnswerId != null
                                    && dto.getAnswerId().equals(acceptedAnswerId)
                    );
                    answer.setQuestion(question);
                    return answer;
                })
                .toList();

        return new ArrayList<>(answers);
    }

    public int importQuestions(String tag) {
        String url = baseUrl
                + "?pagesize=" + pageSize
                + "&order=" + order
                + "&sort=" + sort
                + "&site=" + site
                + "&tagged=" + tag;

        StackOverflowResponseDto response =
                restTemplate.getForObject(url, StackOverflowResponseDto.class);

        if (response == null || response.getItems() == null) {
            throw new RuntimeException("Failed to import StackOverflow questions");
        }

        List<Question> questions = response.getItems()
                .stream()
                .map(this::mapQuestion)
                .filter(Objects::nonNull)
                .toList();

        questionRepository.saveAll(questions);

        return questions.size();
    }

    private Question mapQuestion(StackOverflowQuestionDto dto) {
        Question question = new Question();

        question.setContent(clean(dto.getTitle()));
        question.setCategory("StackOverflow");
        question.setDifficulty("Senior");
        question.setExplanation("Imported from StackOverflow API");

        List<Answer> answers = fetchAnswersForQuestion(
                question,
                dto.getQuestionId(),
                dto.getAcceptedAnswerId()
        );

        if (answers.isEmpty() || answers.stream().noneMatch(Answer::isCorrect)) {
            return null;
        }

        Collections.shuffle(answers);
        question.setAnswers(answers);

        return question;
    }

    private String clean(
            String value
    ) {

        if (
                value == null
        ) {
            return "";
        }

        return value
                .replace(
                        "&quot;",
                        "\""
                )
                .replace(
                        "&#039;",
                        "'"
                )
                .replace(
                        "&amp;",
                        "&"
                );

    }

    private String removeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replaceAll("<[^>]*>", "")
                .replace("&quot;", "\"")
                .replace("&#039;", "'")
                .replace("&amp;", "&")
                .trim();
    }

    private String shorten(String value) {
        if (value == null) {
            return "";
        }

        int maxLength = 700;

        if (value.length() <= maxLength) {
            return value;
        }

        return value.substring(0, maxLength) + "...";
    }

}
