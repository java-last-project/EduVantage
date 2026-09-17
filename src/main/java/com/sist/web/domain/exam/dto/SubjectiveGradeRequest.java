package com.sist.web.domain.exam.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubjectiveGradeRequest {
    private Integer score;
    private Boolean correct;
}
