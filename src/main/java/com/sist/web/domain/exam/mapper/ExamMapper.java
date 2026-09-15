package com.sist.web.domain.exam.mapper;

import java.util.*;

import com.sist.web.domain.exam.vo.ExamUserAnswerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;
import com.sist.web.domain.exam.vo.ScheduledExamVO;

@Mapper
@Repository
public interface ExamMapper {
	
	public List<ExamQuestionVO> examDetailData(Map<String,Object> map);
	public void insertEnrollment(ExamEnrollmentVO vo);
	public String getScheduledExamTitle(Integer examNo);
	public ScheduledExamVO getScheduledExam(Integer examNo);
	public int countScheduledExamQuestions(Integer examNo);
	public ExamEnrollmentVO findActiveEnrollment(Map<String,Object> map);
	public ExamEnrollmentVO findCompletedEnrollment(Map<String,Object> map);
	public ExamEnrollmentVO getEnrollmentForMember(Map<String,Object> map);
	public List<ExamQuestionVO> getQuestionForGrading(Map<String,Object> map);
	public void insertUserAnswers(List<ExamUserAnswerVO> answers);
	public int updateEnrollmentFinish(ExamEnrollmentVO vo);
	public Map<String, Object> selectExamResultMaster(Map<String,Object> map);
	public List<Map<String, Object>> selectExamResultDetails(int enrollmentNo);
	public List<Map<String, Object>> selectMyExamList(int memberId);
	public void insertAiExamQuestions(Map<String,Object> map);
	public List<ExamQuestionVO> examDetailDataByEnrollment(Map<String,Object> map);
	public void deleteUserAnswers(Integer enrollmentNo);
	public List<ExamQuestionVO> selectAiWrongQuestionMetadata(Map<String,Object> map);
}
