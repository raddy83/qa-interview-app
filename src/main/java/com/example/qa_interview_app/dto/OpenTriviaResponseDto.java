package com.example.qa_interview_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OpenTriviaResponseDto {

    @JsonProperty("response_code")
    private int responseCode;

    private List<OpenTriviaQuestionDto> results;
}

