package com.sist.web.domain.exam.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiExamCreateRequest {
    private String examName,subject;
    private Integer difficulty,questionCount;
    private List<String>keywords;
}
