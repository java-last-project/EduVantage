package com.sist.web.domain.mypage.service;

import java.util.List;

import com.sist.web.domain.mypage.vo.MyCourseEnrollmentVO;
import com.sist.web.domain.mypage.vo.MyMemberVO;

public interface MyPageService {
	public List<MyCourseEnrollmentVO> mypageCourseListData(int member_id);
	public List<MyCourseEnrollmentVO> lastAccessedCourse(int member_id);
	public int enrolledCount(int member_id);
	public MyMemberVO memberProfileData(int member_id);
}
