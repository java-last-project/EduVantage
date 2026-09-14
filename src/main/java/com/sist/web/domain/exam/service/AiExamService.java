package com.sist.web.domain.exam.service;

import com.sist.web.domain.exam.dto.AiExamCreateRequest;
import com.sist.web.domain.exam.dto.AiExamCreateResponse;

public interface AiExamService {
    public AiExamCreateResponse createExam(AiExamCreateRequest request,Integer memberId);
}
