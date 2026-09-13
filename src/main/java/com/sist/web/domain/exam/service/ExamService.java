package com.sist.web.domain.exam.service;

import java.util.*;

import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;

public interface ExamService {
	public List<ExamQuestionVO> examDetailData(Integer examNo,Integer theme,int count);
	public ExamEnrollmentVO getOrCreateEnrollment(int memberId, Integer examNo, Integer theme);
	public String getExamTitle(Integer examNo);
	public Map<String, Object> submitExam(Map<String, Object> params);
	public Map<String, Object> getExamResultData(int enrollmentNo);
	public List<Map<String, Object>> getMyExamList(int memberId);
}
