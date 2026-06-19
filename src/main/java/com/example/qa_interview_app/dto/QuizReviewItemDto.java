package com.example.qa_interview_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizReviewItemDto {
    private String questionContent;
    private String selectedAnswerContent;
    private String correctAnswerContent;
    private String explanation;
    private String category;
}
