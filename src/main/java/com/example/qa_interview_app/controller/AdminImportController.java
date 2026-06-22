package com.example.qa_interview_app.controller;

import com.example.qa_interview_app.service.StackOverflowImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/import")
public class AdminImportController {

    private final StackOverflowImportService service;

    @PostMapping("/stackoverflow")
    public String importQuestions(@RequestParam String tag) {
        service.importQuestions(tag);
        return "redirect:/";
    }
}
