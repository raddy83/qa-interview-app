package com.example.qa_interview_app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionImportDto {

    private String category;
    private String difficulty;
    private String content;
    private String explanation;
    private List<AnswerImportDto> answers;
}
