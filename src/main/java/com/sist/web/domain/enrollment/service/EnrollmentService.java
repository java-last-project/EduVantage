package com.sist.web.domain.enrollment.service;

import java.util.List;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.vo.CourseEvaluationLikeVO;
import com.sist.web.domain.enrollment.vo.CourseEvaluationVO;

public interface EnrollmentService {
	public CourseVO courseDetailData(int course_no);
	public List<CourseEvaluationVO> evaluationListData(int page,int course_no,int member_id);
	public String courseTitleData(int course_no);
	//public int evaluationCount(int course_no);
	public int[] pages(int page,int course_no);
	public void evaluationInsert(CourseEvaluationVO vo);
	public void evaluationDelete(int ce_no, int course_no);
	public void evaluationUpdate(CourseEvaluationVO vo);
	public void evalLikeOn(CourseEvaluationLikeVO vo);
	public void evalLikeOff(int ce_no,int member_id);
	public CourseVO courseData(int course_no);
	public double courseStarData(int course_no);
	public CourseEvaluationVO evaluationMyData(int course_no,int member_id);
}
