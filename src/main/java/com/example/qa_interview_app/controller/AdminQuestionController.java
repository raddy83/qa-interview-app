package com.example.qa_interview_app.controller;

import com.example.qa_interview_app.service.OpenTriviaImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/questions")
public class AdminQuestionController {

    private final OpenTriviaImportService openTriviaImportService;

    @PostMapping("/import-api")
    public String importFromApi(@RequestParam(defaultValue = "20") int amount) {
        openTriviaImportService.importQuestions(amount);
        return "redirect:/";
    }
}
