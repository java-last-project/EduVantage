package com.sist.web.domain.instructor.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.instructor.mapper.InstructorMapper;
import com.sist.web.domain.member.vo.MemberVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService
{
	private final InstructorMapper iMapper;
	
	@Override
	public MemberVO InstProfileData(int member_id) {
		// TODO Auto-generated method stub
		return iMapper.InstProfileData(member_id);
	}

	@Override
	public List<Map<String, Object>> InstCourseDataList(int member_id) {
		// TODO Auto-generated method stub
		return iMapper.InstCourseDataList(member_id);
	}

	@Override
	public List<Map<String, Object>> InstCourseEnrollStudList(int course_no) {
		// TODO Auto-generated method stub
		return iMapper.InstCourseEnrollStudList(course_no);
	}

	@Override
	public CourseVO InstCourseDetailData(int course_no) {
		// TODO Auto-generated method stub
		return iMapper.InstCourseDetailData(course_no);
	}

}
