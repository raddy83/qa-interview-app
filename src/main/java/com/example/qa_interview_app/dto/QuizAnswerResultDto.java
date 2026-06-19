package com.example.qa_interview_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizAnswerResultDto {

    private Long questionId;
    private Long selectedAnswerId;
    private boolean correct;
}
