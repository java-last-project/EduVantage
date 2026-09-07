package com.sist.web.domain.instructor.service;

import java.util.*;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.member.vo.MemberVO;

public interface InstructorService {
	public MemberVO InstProfileData(int member_id);
	public List<Map<String, Object>> InstCourseDataList(int member_id);
	public List<Map<String, Object>> InstCourseEnrollStudList(int course_no);
	public CourseVO InstCourseDetailData(int course_no);
}
