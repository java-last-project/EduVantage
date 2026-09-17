package com.sist.web.domain.exam.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ExamGradingMapper {
    public List<Map<String, Object>> selectPendingSubjectiveList(int graderId);
    public int claimGradingTask(Map<String, Object> params);
    public int releaseGradingClaim(Map<String, Object> params);
    public Map<String,Object> selectClaimedAnswer(Map<String, Object> params);
    public int gradeSubjectiveAnswer(Map<String, Object> params);
    public int countRemainingPending(int enrollmentNo);
    public int finalizeEnrollmentScore(int enrollmentNo);
    public int finalizePracticeEnrollmentScore(int enrollmentNo);
}
