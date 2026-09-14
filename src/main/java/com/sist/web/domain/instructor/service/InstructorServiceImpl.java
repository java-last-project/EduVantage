package com.sist.web.domain.instructor.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.vo.CourseQnaReplyVO;
import com.sist.web.domain.enrollment.vo.CourseQnaVO;
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

	@Override
	public void instUpdateCourseData(CourseVO vo) {
		// TODO Auto-generated method stub
		iMapper.instUpdateCourseData(vo);
	}
	
	@Override
	public void instInsertNewCourse(CourseVO vo) {
		// TODO Auto-generated method stub
		iMapper.instInsertNewCourse(vo);
	}

	@Override
	public List<Map<String, Object>> instQnaListData(int member_id, int start) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("member_id", member_id);
		map.put("start", start);
		
		return iMapper.instQnaListData(map);
	}
	
	@Override
	public List<Map<String, Object>> instQnaListData(int member_id, int start, String title, String status) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("member_id", member_id);
		map.put("start", start);
		map.put("title", title);
		map.put("status", status);
		
		return iMapper.instQnaListData(map);
	}

	@Override
	public Map<String, Object> instQnaDetailData(int no) {
		// TODO Auto-generated method stub
		return iMapper.instQnaDetailData(no);
	}

	@Override
	public List<String> instQnaFilterCourse(int member_id) {
		// TODO Auto-generated method stub
		return iMapper.instQnaFilterCourse(member_id);
	}
	
	@Override
	public int instCountQnaList(int member_id) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("member_id", member_id);
		
		return iMapper.instCountQnaList(map);
	}

	@Override
	public int instCountQnaList(int member_id, String title, String status) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("member_id", member_id);
		map.put("title", title);
		map.put("status", status);
		
		return iMapper.instCountQnaList(map);
	}

	@Override
	public CourseQnaReplyVO instQnaAnswerData(int no) {
		// TODO Auto-generated method stub
		return iMapper.instQnaAnswerData(no);
	}

	@Override
	@Transactional
	public void instQnaAnswerInsert(CourseQnaReplyVO vo, int no) {
		// TODO Auto-generated method stub
		iMapper.instQnaAnswerInsert(vo);
		iMapper.instQnaUpdateStatus(no);
		
	}

	@Override
	public void instCourseNewsInsert(int course_id, String subject, String content) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("course_id", course_id);
		map.put("subject", subject);
		map.put("content", content);
		
		iMapper.instCourseNewsInsert(map);
	}

	@Override
	public void instCourseNewsInsert(int course_id, String subject, String content, String filename,
			long filesize) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("course_id", course_id);
		map.put("subject", subject);
		map.put("content", content);
		map.put("filename", filename);
		map.put("filesize", filesize);
		
		iMapper.instCourseNewsInsertFile(map);
	}

	@Override
	public List<Map<String, Object>> instCourseNewsListData(int course_id) {
		// TODO Auto-generated method stub
		return iMapper.instCourseNewsListData(course_id);
	}

	@Override
	public Map<String, Object> instCourseNewsDetail(int no) {
		// TODO Auto-generated method stub
		return iMapper.instCourseNewsDetail(no);
	}

	@Override
	public void instCourseNewsHitUp(int no) {
		// TODO Auto-generated method stub
		iMapper.instCourseNewsHitUp(no);
	}

	@Override
	@Transactional
	public void instProfileUpdate(MemberVO vo) {
		// TODO Auto-generated method stub
		iMapper.instProfileUpdate(vo);
		
		if(vo.getPassword()!=null && !vo.getPassword().isEmpty())
		{
			// 비밀번호 업데이트 문장 실행
			iMapper.instProfilePwdUpdate(vo);
		}
	}

	
	

}
