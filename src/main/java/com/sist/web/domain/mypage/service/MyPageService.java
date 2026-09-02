package com.sist.web.domain.mypage.service;

import java.util.List;

import com.sist.web.domain.mypage.vo.CourseEnrollmentVO;
import com.sist.web.domain.mypage.vo.MyMemberVO;

public interface MyPageService {
	public List<CourseEnrollmentVO> mypageCourseListData(int member_id);
	public List<CourseEnrollmentVO> lastAccessedCourse(int member_id);
	public int enrolledCount(int member_id);
	public MyMemberVO memberProfileData(int member_id);
}
