package com.sist.web.domain.exam.service;

import java.util.List;
import java.util.Map;

public interface ExamGradingService {
    List<Map<String, Object>> getPendingSubjectiveList(int graderId);
    boolean claimTask(int answerNo, int graderId);
    boolean releaseClaim(int answerNo, int graderId);
    void gradeSubjective(int answerNo, int graderId, int score);
}
