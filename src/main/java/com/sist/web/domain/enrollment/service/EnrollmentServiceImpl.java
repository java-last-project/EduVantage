package com.sist.web.domain.enrollment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.mapper.EnrollmentMapper;
import com.sist.web.domain.enrollment.vo.CourseEvaluationLikeVO;
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
	public List<CourseEvaluationVO> evaluationListData(int page,int course_no,int member_id) {
		// TODO Auto-generated method stub
		final int ROWSIZE=3;
		int start=(page*ROWSIZE)-ROWSIZE;
		return eMapper.evaluationListData(start,course_no,member_id);
	}
	@Override
	public int[] pages(int page, int course_no) {
		// TODO Auto-generated method stub
		int count=eMapper.evaluationCount(course_no);
		int totalpage=(int)Math.ceil(count/3.0);
		final int BLOCK=10;
		int startpage=((page-1)/BLOCK*BLOCK)+1;
		int endpage=((page-1)/BLOCK*BLOCK)+BLOCK;
		if(endpage>totalpage) endpage=totalpage;
		
		int[] pages= {page,totalpage,startpage,endpage,count};
		return pages;
	}
	
	@Override
	@Transactional
	public void evaluationInsert(CourseEvaluationVO vo) {
		// TODO Auto-generated method stub
		eMapper.evaluationInsert(vo);
		eMapper.courseStarUpdate(vo.getCourse_no());
	}
	
	@Override
	@Transactional
	public void evalLikeOn(CourseEvaluationLikeVO vo) {
		// TODO Auto-generated method stub
		eMapper.evalLikeInsert(vo);
		eMapper.evalLikeIncrement(vo.getCe_no());
	}
	
	@Override
	@Transactional
	public void evalLikeOff(int ce_no,int member_id) {
		// TODO Auto-generated method stub
		eMapper.evalLikeDecrement(ce_no);
		eMapper.evalLikeDelete(ce_no,member_id);
	}
	@Override
	public CourseVO courseData(int course_no) {
		// TODO Auto-generated method stub
		return eMapper.courseData(course_no);
	}
	@Override
	public double courseStarData(int course_no) {
		// TODO Auto-generated method stub
		return eMapper.courseStarData(course_no);
	}
	
	@Override
	@Transactional
	public void evaluationDelete(int ce_no, int course_no) {
		// TODO Auto-generated method stub
		eMapper.evalLikeDeleteAll(ce_no);
		eMapper.evaluationDelete(ce_no);
		eMapper.courseStarUpdate(course_no);
	}
	@Override
	public CourseEvaluationVO evaluationMyData(int course_no, int member_id) {
		// TODO Auto-generated method stub
		return eMapper.evaluationMyData(course_no, member_id);
	}
	
	@Override
	@Transactional
	public void evaluationUpdate(CourseEvaluationVO vo) {
		// TODO Auto-generated method stub
		eMapper.evaluationUpdate(vo);
		eMapper.courseStarUpdate(vo.getCourse_no());
	}

}
