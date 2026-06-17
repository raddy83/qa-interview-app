package com.example.qa_interview_app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuizSession {

    private List<Long> questionIds = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
}