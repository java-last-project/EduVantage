package com.sist.web.domain.enrollment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.vo.CourseEvaluationLikeVO;
import com.sist.web.domain.enrollment.vo.CourseEvaluationVO;

@Mapper
@Repository
public interface EnrollmentMapper {
	public CourseVO courseDetailData(int course_no);
	public List<CourseEvaluationVO> evaluationListData(
			@Param("start") int start,
			@Param("course_no") int course_no,
			@Param("member_id") int member_id
			);
	
	@Select("SELECT title FROM course WHERE no=#{course_no}")
	public String courseTitleData(int course_no);
	
	@Select("SELECT COUNT(*) FROM course_evaluation WHERE course_no=#{course_no}")
	public int evaluationCount(int course_no);
	
	public void evaluationInsert(CourseEvaluationVO vo);
	public void courseStarUpdate(int course_no);
	
	@Delete("DELETE FROM course_evaluation WHERE no=#{no}")
	public void evaluationDelete(int no);
	
	@Delete("DELETE FROM course_evaluation_like WHERE ce_no=#{ce_no}")
	public void evalLikeDeleteAll(int ce_no);
	
	@Update("UPDATE course_evaluation "
			+ "SET rating=#{rating},review=#{review} WHERE no=#{no}")
	public void evaluationUpdate(CourseEvaluationVO vo);
	
	@Select("SELECT star FROM course WHERE no=#{course_no}")
	public double courseStarData(int course_no);	
	
	public void evalLikeInsert(CourseEvaluationLikeVO vo);
	public void evalLikeIncrement(int ce_no);
	
	public void evalLikeDecrement(int ce_no);
	public void evalLikeDelete(@Param("ce_no") int ce_no,@Param("member_id") int member_id);
	
	public CourseVO courseData(int course_no);
	
	public CourseEvaluationVO evaluationMyData(
			@Param("course_no") int course_no,
			@Param("member_id") int member_id
		);
	
}
