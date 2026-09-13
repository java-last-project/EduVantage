package com.sist.web.domain.exam.mapper;

import java.util.*;

import com.sist.web.domain.exam.vo.ExamUserAnswerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;

@Mapper
@Repository
public interface ExamMapper {
	
	public List<ExamQuestionVO> examDetailData(Map<String,Object> map);
	public void insertEnrollment(ExamEnrollmentVO vo);
	public String getScheduledExamTitle(Integer examNo);
	public ExamEnrollmentVO findActiveEnrollment(Map<String,Object> map);
	public List<ExamQuestionVO> getQuestionForGrading(List<Integer> qno);
	public void insertUserAnswers(List<ExamUserAnswerVO> answers);
	public void updateEnrollmentFinish(ExamEnrollmentVO vo);
	public Map<String, Object> selectExamResultMaster(int enrollmentNo);
	public List<Map<String, Object>> selectExamResultDetails(int enrollmentNo);
	public List<Map<String, Object>> selectMyExamList(int memberId);
}
