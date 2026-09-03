package com.sist.web.domain.mypage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.domain.mypage.vo.MyCourseEnrollmentVO;
import com.sist.web.domain.mypage.vo.MyMemberVO;
import com.sist.web.domain.mypage.mapper.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
	private final MyPageMapper mMapper;
	@Override
	public List<MyCourseEnrollmentVO> mypageCourseListData(int member_id) {
		// TODO Auto-generated method stub
		return mMapper.mypageCourseListData(member_id);
	}
	@Override
	public int enrolledCount(int member_id) {
		// TODO Auto-generated method stub
		return mMapper.enrolledCount(member_id);
	}
	@Override
	public List<MyCourseEnrollmentVO> lastAccessedCourse(int member_id) {
		// TODO Auto-generated method stub
		return mMapper.lastAccessedCourse(member_id);
	}
	@Override
	public MyMemberVO memberProfileData(int member_id) {
		// TODO Auto-generated method stub
		return mMapper.memberProfileData(member_id);
	}

}
