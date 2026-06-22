package com.example.qa_interview_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StackOverflowQuestionDto {

    private String title;

    private List<String> tags;

    @JsonProperty("question_id")
    private Long questionId;

    @JsonProperty("accepted_answer_id")
    private Long acceptedAnswerId;

}
