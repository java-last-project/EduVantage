package com.sist.web.domain.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sist.web.domain.exam.mapper.ExamGradingMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamGradingServiceImpl implements ExamGradingService {
    private final ExamGradingMapper gradingMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPendingSubjectiveList(int graderId) {
        return gradingMapper.selectPendingSubjectiveList(graderId);
    }

    @Override
    @Transactional
    public boolean claimTask(int answerNo, int graderId) {
        return gradingMapper.claimGradingTask(gradingParams(answerNo, graderId)) > 0;
    }

    @Override
    @Transactional
    public boolean releaseClaim(int answerNo, int graderId) {
        return gradingMapper.releaseGradingClaim(gradingParams(answerNo, graderId)) > 0;
    }

    @Override
    @Transactional
    public void gradeSubjective(int answerNo, int graderId, int score) {
        Map<String, Object> params = gradingParams(answerNo, graderId);
        params.put("score", score);

        Integer enrollmentNo = gradingMapper.selectClaimedEnrollmentNo(params);
        if (enrollmentNo == null) {
            throw new IllegalStateException("선점하지 않았거나 이미 처리된 답안입니다.");
        }

        if (gradingMapper.gradeSubjectiveAnswer(params) == 0) {
            throw new IllegalStateException("선점하지 않았거나 이미 처리된 답안입니다.");
        }

        if (gradingMapper.countRemainingPending(enrollmentNo) == 0) {
            gradingMapper.finalizeEnrollmentScore(enrollmentNo);
        }
    }

    private Map<String, Object> gradingParams(int answerNo, int graderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("answerNo", answerNo);
        params.put("graderId", graderId);
        return params;
    }
}
