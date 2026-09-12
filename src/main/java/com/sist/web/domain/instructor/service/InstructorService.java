package com.sist.web.domain.instructor.service;

import java.util.*;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.vo.CourseQnaReplyVO;
import com.sist.web.domain.enrollment.vo.CourseQnaVO;
import com.sist.web.domain.member.vo.MemberVO;

public interface InstructorService {
	public MemberVO InstProfileData(int member_id);
	public List<Map<String, Object>> InstCourseDataList(int member_id);
	public List<Map<String, Object>> InstCourseEnrollStudList(int course_no);
	public CourseVO InstCourseDetailData(int course_no);
	public void instUpdateCourseData(CourseVO vo);
	public void instInsertNewCourse(CourseVO vo);
	
	public List<Map<String, Object>> instQnaListData(int member_id, int start);
	public List<Map<String, Object>> instQnaListData(int member_id, int start, String title, String status);
	public int instCountQnaList(int member_id);
	public int instCountQnaList(int member_id, String title, String status);
	
	public Map<String, Object> instQnaDetailData(int no);
	public List<String> instQnaFilterCourse(int member_id);
	public CourseQnaReplyVO instQnaAnswerData(int no);
	
	public void instQnaAnswerInsert(CourseQnaReplyVO vo, int no);
}
