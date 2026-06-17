package com.example.qa_interview_app.controller;

import com.example.qa_interview_app.model.Question;
import com.example.qa_interview_app.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuizController.class)
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuestionService questionService;

    @Test
    void shouldDisplayHomePageWithQuestions() throws Exception {
        Question question = new Question();
        question.setId(1L);
        question.setCategory("Java");
        question.setDifficulty("Senior");
        question.setContent("What is JVM?");

        when(questionService.getAllQuestions())
                .thenReturn(List.of(question));

        when(questionService.getAllCategories())
                .thenReturn(List.of("Java"));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("questions"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("What is JVM?")));
    }
}