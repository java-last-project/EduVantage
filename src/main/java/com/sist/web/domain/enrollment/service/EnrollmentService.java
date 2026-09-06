package com.sist.web.domain.enrollment.service;

import java.util.List;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.vo.CourseEvaluationVO;

public interface EnrollmentService {
	public CourseVO courseDetailData(int course_no);
	public List<CourseEvaluationVO> evaluationListData(int course_no);
	public String courseTitleData(int course_no);
	public int evaluationCount(int course_no);
}
