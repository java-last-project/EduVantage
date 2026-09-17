package com.sist.web.domain.exam.service;

import java.util.*;

import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;

public interface ExamService {
	public List<ExamQuestionVO> examDetailData(Integer examNo,Integer theme,int count);
	public ExamEnrollmentVO getOrCreateEnrollment(int memberId, Integer examNo, Integer theme, int count);
	public List<ExamQuestionVO> getPracticeExamQuestions(int memberId,int enrollmentNo);
	public String getExamTitle(Integer examNo);
	public int getExamLimitMinutes(Integer examNo);
	public Map<String, Object> submitExam(int memberId,Map<String, Object> params);
	public Map<String, Object> getExamResultData(int memberId,int enrollmentNo);
	public Integer getScheduledExamResult(int memberId,int examNo);
	public List<Map<String, Object>> getMyExamList(int memberId);
	public ExamEnrollmentVO createAiEnrollment(Integer memberId, List<Integer> questionNos);
	public Map<String,Object> getAiExamDetailData(int memberId,int enrollmentNo);
}
