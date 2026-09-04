package com.sist.web.domain.mypage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.domain.enrollment.vo.*;
import com.sist.web.domain.member.vo.MemberVO;
import com.sist.web.domain.mypage.vo.CourseCartVO;
import com.sist.web.domain.mypage.vo.CoursePaymentVO;
import com.sist.web.domain.mypage.vo.MyMemberVO;
import com.sist.web.domain.mypage.mapper.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
	private final MyPageMapper mMapper;
	@Override
	public List<CourseEnrollmentVO> mypageCourseListData(int member_id) {
		// TODO Auto-generated method stub
		return mMapper.mypageCourseListData(member_id);
	}
	@Override
	public int enrolledCount(int member_id) {
		// TODO Auto-generated method stub
		return mMapper.enrolledCount(member_id);
	}
	@Override
	public List<CourseEnrollmentVO> lastAccessedCourse(int member_id) {
		// TODO Auto-generated method stub
		return mMapper.lastAccessedCourse(member_id);
	}
	@Override
	public MyMemberVO memberProfileData(int member_id) {
		// TODO Auto-generated method stub
		return mMapper.memberProfileData(member_id);
	}
	@Override
	public void memberUpdateData(MemberVO vo) {
		// TODO Auto-generated method stub
		mMapper.memberUpdateData(vo);
	}
	@Override
	public List<CoursePaymentVO> coursePaymentListData(int page,int member_id) {
		// TODO Auto-generated method stub
		final int ROWSIZE=3;
		int start=(page*ROWSIZE)-ROWSIZE;
		return mMapper.coursePaymentListData(start,member_id);
	}
	@Override
	public int[] pages(String type, int page,int member_id) {
		// TODO Auto-generated method stub
		int count=0;
		if(type.equals("course_payment")) count=mMapper.coursePaymentRowCount(member_id);
		else if(type.equals("course_cart")) count=mMapper.courseCartRowCount(member_id);
		
		int totalpage=(int)Math.ceil(count/3.0);
		final int BLOCK=10;
		int startpage=((page-1)/BLOCK*BLOCK)+1;
		int endpage=((page-1)/BLOCK*BLOCK)+BLOCK;
		if(endpage>totalpage) endpage=totalpage;
		
		int[] pages= {page,totalpage,startpage,endpage,count};
		return pages;
	}
	@Override
	public List<CourseCartVO> courseCartListData(int page, int member_id) {
		// TODO Auto-generated method stub
		final int ROWSIZE=3;
		int start=(page*ROWSIZE)-ROWSIZE;
		return mMapper.courseCartListData(start, member_id);
	}

}
