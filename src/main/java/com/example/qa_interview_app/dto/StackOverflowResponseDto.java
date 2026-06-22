package com.example.qa_interview_app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StackOverflowResponseDto {

    private List<StackOverflowQuestionDto> items;
}
