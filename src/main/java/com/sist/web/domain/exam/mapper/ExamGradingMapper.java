package com.sist.web.domain.exam.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ExamGradingMapper {
    List<Map<String, Object>> selectPendingSubjectiveList(int graderId);
    int claimGradingTask(Map<String, Object> params);
    int releaseGradingClaim(Map<String, Object> params);
    Integer selectClaimedEnrollmentNo(Map<String, Object> params);
    int gradeSubjectiveAnswer(Map<String, Object> params);
    int countRemainingPending(int enrollmentNo);
    int finalizeEnrollmentScore(int enrollmentNo);
}
