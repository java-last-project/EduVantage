package com.sist.web.domain.mypage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.domain.enrollment.vo.*;
import com.sist.web.domain.member.vo.MemberVO;
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

}
