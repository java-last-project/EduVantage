package com.sist.web.domain.mypage.service;

import java.util.List;

import com.sist.web.domain.enrollment.vo.*;
import com.sist.web.domain.member.vo.MemberVO;
import com.sist.web.domain.mypage.vo.CourseCartVO;
import com.sist.web.domain.mypage.vo.CoursePaymentVO;
import com.sist.web.domain.mypage.vo.MyMemberVO;

public interface MyPageService {
	public List<CourseEnrollmentVO> mypageCourseListData(int member_id);
	public List<CourseEnrollmentVO> lastAccessedCourse(int member_id);
	public int enrolledCount(int member_id);
	public MyMemberVO memberProfileData(int member_id);
	public void memberUpdateData(MemberVO vo);
	public List<CoursePaymentVO> coursePaymentListData(int page,int member_id);
	public int[] pages(String type, int page,int member_id);
	public List<CourseCartVO> courseCartListData(int page,int member_id);
}
