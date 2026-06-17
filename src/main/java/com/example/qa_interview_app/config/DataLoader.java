package com.example.qa_interview_app.config;

import com.example.qa_interview_app.model.Answer;
import com.example.qa_interview_app.model.Question;
import com.example.qa_interview_app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final QuestionRepository questionRepository;

    @Override
    public void run(String... args) {

        Question question1 = new Question();
        question1.setCategory("Selenium");
        question1.setDifficulty("Senior");
        question1.setContent("What is the Page Object Model pattern?");
        question1.setExplanation("Page Object Model separates test logic from page locators and page actions.");

        Answer q1a1 = new Answer();
        q1a1.setContent("A design pattern that separates page structure and test logic");
        q1a1.setCorrect(true);
        q1a1.setQuestion(question1);

        Answer q1a2 = new Answer();
        q1a2.setContent("A database optimization pattern");
        q1a2.setCorrect(false);
        q1a2.setQuestion(question1);

        Answer q1a3 = new Answer();
        q1a3.setContent("A CI/CD deployment strategy");
        q1a3.setCorrect(false);
        q1a3.setQuestion(question1);

        question1.setAnswers(List.of(q1a1, q1a2, q1a3));

        Question question2 = new Question();
        question2.setCategory("REST API");
        question2.setDifficulty("Senior");
        question2.setContent("What status code should be returned when a resource is successfully created?");
        question2.setExplanation("HTTP 201 Created means that the request succeeded and a new resource was created.");

        Answer q2a1 = new Answer();
        q2a1.setContent("200 OK");
        q2a1.setCorrect(false);
        q2a1.setQuestion(question2);

        Answer q2a2 = new Answer();
        q2a2.setContent("201 Created");
        q2a2.setCorrect(true);
        q2a2.setQuestion(question2);

        Answer q2a3 = new Answer();
        q2a3.setContent("404 Not Found");
        q2a3.setCorrect(false);
        q2a3.setQuestion(question2);

        question2.setAnswers(List.of(q2a1, q2a2, q2a3));

        Question question3 = new Question();
        question3.setCategory("Java");
        question3.setDifficulty("Senior");
        question3.setContent("What is the main difference between == and equals() in Java?");
        question3.setExplanation("The == operator compares object references, while equals() compares object values if properly overridden.");

        Answer q3a1 = new Answer();
        q3a1.setContent("== compares references, equals() compares values");
        q3a1.setCorrect(true);
        q3a1.setQuestion(question3);

        Answer q3a2 = new Answer();
        q3a2.setContent("They always work exactly the same");
        q3a2.setCorrect(false);
        q3a2.setQuestion(question3);

        Answer q3a3 = new Answer();
        q3a3.setContent("equals() compares memory addresses only");
        q3a3.setCorrect(false);
        q3a3.setQuestion(question3);

        question3.setAnswers(List.of(q3a1, q3a2, q3a3));

        questionRepository.saveAll(List.of(question1, question2, question3));
    }
}