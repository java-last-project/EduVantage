package com.sist.web.domain.exam.service;

import java.util.List;

import com.sist.web.domain.exam.vo.ExamQuestionVO;
import com.sist.web.domain.exam.vo.ScheduledExamVO;

public interface InstructorScheduledExamService {
	public List<ScheduledExamVO> getScheduledExamList(int instructorId);
	public ScheduledExamVO getScheduledExamDetail(int instructorId,int examNo);
	public List<ExamQuestionVO> getQuestionBank(Integer theme);
	public int insertScheduledExam(int instructorId,ScheduledExamVO vo);
	public void updateScheduledExam(int instructorId,int examNo,ScheduledExamVO vo);
	public void deleteScheduledExam(int instructorId,int examNo);
}
