package com.example.qa_interview_app.service;

import com.example.qa_interview_app.model.Question;
import com.example.qa_interview_app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collections;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
    }

    public List<Question> getQuestionsByCategory(String category) {
        return questionRepository.findByCategory(category);
    }

    public List<Question> getQuestionsByDifficulty(String difficulty) {
        return questionRepository.findByDifficulty(difficulty);
    }

    public List<Question> getQuestionsByCategoryAndDifficulty(String category, String difficulty) {
        return questionRepository.findByCategoryAndDifficulty(category, difficulty);
    }

    public boolean checkAnswer(Long questionId, Long answerId) {
        Question question = getQuestionById(questionId);

        return question.getAnswers()
                .stream()
                .anyMatch(answer ->
                        answer.getId().equals(answerId)
                                && answer.isCorrect()
                );
    }

    public List<String> getAllCategories() {
        return questionRepository.findAll()
                .stream()
                .map(Question::getCategory)
                .distinct()
                .toList();
    }

    public List<Question> getRandomQuestions(int limit) {
        List<Question> questions = questionRepository.findAll();

        Collections.shuffle(questions);

        return questions.stream()
                .limit(limit)
                .toList();
    }

    public Question getQuestionByIdWithShuffledAnswers(Long id) {
        Question question = getQuestionById(id);

        Collections.shuffle(question.getAnswers());

        return question;
    }
}