package com.sist.web.domain.exam.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.exam.vo.ExamQuestionVO;
import com.sist.web.domain.exam.vo.ScheduledExamMapVO;
import com.sist.web.domain.exam.vo.ScheduledExamVO;

@Mapper
@Repository
public interface InstructorScheduledExamMapper {
	public List<ScheduledExamVO> selectInstructorScheduledExamList(@Param("instructorId") int instructorId);
	public ScheduledExamVO selectInstructorScheduledExamDetail(Map<String,Object> map);
	public List<Integer> selectScheduledQuestionNos(Map<String,Object> map);
	public List<ExamQuestionVO> selectQuestionBank(@Param("theme") Integer theme);
	public List<ExamQuestionVO> selectQuestionsByNos(List<Integer> questionNos);
	public void insertScheduledExam(ScheduledExamVO vo);
	public void insertScheduledExamMaps(List<ScheduledExamMapVO> list);
	public int updateScheduledExam(ScheduledExamVO vo);
	public int updateStartedExamCloseDate(ScheduledExamVO vo);
	public void deleteScheduledExamMaps(@Param("examNo") int examNo);
	public void deleteScheduledExamSubscriptions(@Param("examNo") int examNo);
	public int countScheduledExamEnrollments(@Param("examNo") int examNo);
	public int deleteScheduledExam(Map<String,Object> map);
}
