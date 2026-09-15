package com.sist.web.domain.exam.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiExamCreateResponse {
    private String examName,subject;
    private Integer difficulty,enrollmentNo;
    private List<AiExamQuestionResponse> questions;
    private List<Integer> questionNos;
}
