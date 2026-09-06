package com.sist.web.domain.enrollment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.mapper.EnrollmentMapper;
import com.sist.web.domain.enrollment.vo.CourseEvaluationVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
	private final EnrollmentMapper eMapper;
	@Override
	public CourseVO courseDetailData(int course_no) {
		// TODO Auto-generated method stub
		return eMapper.courseDetailData(course_no);
	}
	@Override
	public String courseTitleData(int course_no) {
		// TODO Auto-generated method stub
		return eMapper.courseTitleData(course_no);
	}
	@Override
	public List<CourseEvaluationVO> evaluationListData(int course_no) {
		// TODO Auto-generated method stub
		return eMapper.evaluationListData(course_no);
	}
	@Override
	public int evaluationCount(int course_no) {
		// TODO Auto-generated method stub
		return eMapper.evaluationCount(course_no);
	}

}
