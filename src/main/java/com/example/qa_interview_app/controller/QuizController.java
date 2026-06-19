package com.example.qa_interview_app.controller;

import com.example.qa_interview_app.dto.QuizAnswerResultDto;
import com.example.qa_interview_app.dto.QuizReviewItemDto;
import com.example.qa_interview_app.model.Question;
import com.example.qa_interview_app.model.Answer;
import com.example.qa_interview_app.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.qa_interview_app.session.QuizSession;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuestionService questionService;

    @GetMapping("/")
    public String home(
            @RequestParam(required = false) String category,
            Model model
    ) {
        if (category == null || category.isBlank()) {
            model.addAttribute("questions", questionService.getAllQuestions());
        } else {
            model.addAttribute("questions", questionService.getQuestionsByCategory(category));
        }

        model.addAttribute("categories", questionService.getAllCategories());
        model.addAttribute("selectedCategory", category);

        return "index";
    }

    @GetMapping("/questions/{id}")
    public String question(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionService.getQuestionByIdWithShuffledAnswers(id));
        return "question";
    }

    @PostMapping("/questions/{id}/answer")
    public String answer(
            @PathVariable Long id,
            @RequestParam Long answerId,
            Model model
    ) {
        Question question = questionService.getQuestionByIdWithShuffledAnswers(id);
        boolean correct = questionService.checkAnswer(id, answerId);

        model.addAttribute("question", question);
        model.addAttribute("correct", correct);

        return "result";
    }

    @GetMapping("/quiz/start")
    public String startQuiz(HttpSession session) {
        QuizSession quizSession = new QuizSession();

        List<Long> randomQuestionIds = questionService.getRandomQuestions(20)
                .stream()
                .map(Question::getId)
                .toList();

        quizSession.setQuestionIds(randomQuestionIds);
        session.setAttribute("quizSession", quizSession);

        return "redirect:/quiz/question";
    }

    @GetMapping("/quiz/question")
    public String quizQuestion(HttpSession session, Model model) {
        QuizSession quizSession = (QuizSession) session.getAttribute("quizSession");

        if (quizSession == null) {
            return "redirect:/quiz/start";
        }

        if (quizSession.getCurrentQuestionIndex() >= quizSession.getQuestionIds().size()) {
            return "redirect:/quiz/result";
        }

        Long questionId = quizSession.getQuestionIds()
                .get(quizSession.getCurrentQuestionIndex());

        Question question = questionService.getQuestionByIdWithShuffledAnswers(questionId);

        model.addAttribute("question", question);
        model.addAttribute("currentNumber", quizSession.getCurrentQuestionIndex() + 1);
        model.addAttribute("totalQuestions", quizSession.getQuestionIds().size());

        return "quiz-question";
    }

    @PostMapping("/quiz/answer")
    public String quizAnswer(
            @RequestParam Long questionId,
            @RequestParam Long answerId,
            HttpSession session
    ) {
        QuizSession quizSession = (QuizSession) session.getAttribute("quizSession");

        if (quizSession == null) {
            return "redirect:/quiz/start";
        }

        boolean correct = questionService.checkAnswer(questionId, answerId);

        if (correct) {
            quizSession.setScore(quizSession.getScore() + 1);
        }

        quizSession.getAnswers().add(
                new QuizAnswerResultDto(questionId, answerId, correct)
        );

        quizSession.setCurrentQuestionIndex(
                quizSession.getCurrentQuestionIndex() + 1);

        return "redirect:/quiz/question";

    }

    @GetMapping("/quiz/result")
    public String quizResult(HttpSession session, Model model) {
        QuizSession quizSession = (QuizSession) session.getAttribute("quizSession");

        if (quizSession == null) {
            return "redirect:/quiz/start";
        }

        int totalQuestions = quizSession.getQuestionIds().size();
        int score = quizSession.getScore();
        double percentage = totalQuestions == 0 ? 0 : (score * 100.0) / totalQuestions;
        boolean passed = percentage >= 75.0;

        List<QuizReviewItemDto> wrongAnswers = quizSession.getAnswers()
                .stream()
                .filter(answerResult -> !answerResult.isCorrect())
                .map(answerResult -> {
                    Question question = questionService.getQuestionById(answerResult.getQuestionId());

                    String selectedAnswer = question.getAnswers()
                            .stream()
                            .filter(answer -> answer.getId().equals(answerResult.getSelectedAnswerId()))
                            .findFirst()
                            .map(Answer::getContent)
                            .orElse("Unknown answer");

                    String correctAnswer = question.getAnswers()
                            .stream()
                            .filter(Answer::isCorrect)
                            .findFirst()
                            .map(Answer::getContent)
                            .orElse("No correct answer defined");

                    return new QuizReviewItemDto(
                            question.getContent(),
                            selectedAnswer,
                            correctAnswer,
                            question.getExplanation(),
                            question.getCategory()
                    );
                })
                .toList();

        model.addAttribute("score", score);
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("percentage", percentage);
        model.addAttribute("passed", passed);
        model.addAttribute("wrongAnswers", wrongAnswers);

        session.removeAttribute("quizSession");

        return "quiz-result";
    }
}