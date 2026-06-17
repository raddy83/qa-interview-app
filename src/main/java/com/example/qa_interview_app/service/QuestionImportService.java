package com.example.qa_interview_app.service;

import com.example.qa_interview_app.dto.AnswerImportDto;
import com.example.qa_interview_app.dto.QuestionImportDto;
import com.example.qa_interview_app.model.Answer;
import com.example.qa_interview_app.model.Question;
import com.example.qa_interview_app.repository.QuestionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionImportService {

    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    public void importQuestionsFromJson() {
        try {
            ClassPathResource resource = new ClassPathResource("questions/questions.json");

            InputStream inputStream = resource.getInputStream();

            List<QuestionImportDto> importedQuestions = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<QuestionImportDto>>() {}
            );

            List<Question> questions = importedQuestions.stream()
                    .map(this::mapToQuestion)
                    .toList();

            questionRepository.saveAll(questions);

        } catch (Exception e) {
            throw new RuntimeException("Failed to import questions from JSON", e);
        }
    }

    private Question mapToQuestion(QuestionImportDto dto) {
        Question question = new Question();
        question.setCategory(dto.getCategory());
        question.setDifficulty(dto.getDifficulty());
        question.setContent(dto.getContent());
        question.setExplanation(dto.getExplanation());

        List<Answer> answers = dto.getAnswers()
                .stream()
                .map(answerDto -> mapToAnswer(answerDto, question))
                .toList();

        question.setAnswers(answers);

        return question;
    }

    private Answer mapToAnswer(AnswerImportDto dto, Question question) {
        Answer answer = new Answer();
        answer.setContent(dto.getContent());
        answer.setCorrect(dto.isCorrect());
        answer.setQuestion(question);

        return answer;
    }
}