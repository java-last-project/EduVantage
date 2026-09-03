package com.sist.web.domain.enrollment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.vo.CourseEvaluationVO;

@Mapper
@Repository
public interface EnrollmentMapper {
	public CourseVO courseDetailData(int course_no);
	public List<CourseEvaluationVO> evaluationListData(int course_no);
	
	@Select("SELECT title FROM course WHERE no=#{course_no}")
	public String courseTitleData(int course_no);
	
	@Select("SELECT COUNT(*) FROM course_evaluation WHERE course_no=#{course_no}")
	public int evaluationCount(int course_no);
	
}
