package com.example.qa_interview_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StackOverflowAnswerDto {

    private String body;

    @JsonProperty("answer_id")
    private Long answerId;

    @JsonProperty("is_accepted")
    private boolean accepted;
}
