package com.sist.web.domain.exam.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiExamQuestionResponse {
    private Integer answer;
    private String title,description,topic;
    private List<String> options,keywords;
}
