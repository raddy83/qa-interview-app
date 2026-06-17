package com.example.qa_interview_app.config;

import com.example.qa_interview_app.service.QuestionImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionJsonLoader implements CommandLineRunner {

    private final QuestionImportService questionImportService;

    @Override
    public void run(String... args) {
        questionImportService.importQuestionsFromJson();
    }
}
