package com.sist.web.domain.admin.service;

import java.util.*;
import com.sist.web.domain.member.vo.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sist.web.domain.admin.mapper.AdminMapper;
import com.sist.web.domain.course.vo.CourseVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService
{
	private final AdminMapper aMapper;

	@Override
	public int[] getPageData(int page) {
		
		
		int count = aMapper.getCountMember();
		
		int totalpage = (int)(Math.ceil(count/10.0));
		final int BLOCK = 10;
		int startPage = ((page-1)/BLOCK*BLOCK)+1;
		int endPage = ((page-1)/BLOCK*BLOCK)+BLOCK;
		
		if(endPage>totalpage)
			endPage = totalpage;
		
		int[] datas = {page, totalpage, startPage, endPage};
		
		return datas;
	}
	
	@Override
	public int[] getPageData(int page, String authority, String enabled) {
		
		int start = (page*10)-10;
		Map<String, Object> map = new HashMap<>();
		map.put("authority", authority);
		map.put("enabled", enabled);
		map.put("start", start);
		int count = aMapper.getCountFilterMember(map);
		
		int totalpage = (int)(Math.ceil(count/10.0));
		final int BLOCK = 10;
		int startPage = ((page-1)/BLOCK*BLOCK)+1;
		int endPage = ((page-1)/BLOCK*BLOCK)+BLOCK;
		
		if(endPage>totalpage)
			endPage = totalpage;
		
		int[] datas = {page, totalpage, startPage, endPage};
		
		return datas;
	}

	@Override
	public List<Map<String,Object>> adminMemberListData(int page) {
		
		int start = (page*10)-10;
		
		return aMapper.adminMemberListData(start);
	}

	@Override
	public int getTotalMember() {
		
		return aMapper.getCountMember();
	}

	@Override
	public List<Map<String,Object>> adminMemberFindByName(String name) {
		
		return aMapper.adminMemberFindByName(name);
	}

	@Override
	public List<Map<String, Object>> adminMemberFilterListData(String authority, String enabled, int page) {
		
		int start = (page*10)-10;
		Map<String, Object> map = new HashMap<>();
		map.put("authority", authority);
		map.put("enabled", enabled);
		map.put("start", start);
		return aMapper.adminMemberFilterListData(map);
	}

	@Override
	public int getCountFilterMember(String authority, String enabled) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("authority", authority);
		map.put("enabled", Integer.parseInt(enabled));
		return aMapper.getCountFilterMember(map);
	}

	@Override
	public Map<String, Object> adminMemberDetailData(int member_id) {
		
		return aMapper.adminMemberDetailData(member_id);
	}

	@Override
	public void adminUpdateMemberEnabled(int enabled, int member_id) {
		
		MemberVO vo = new MemberVO();
		vo.setEnabled(enabled);
		vo.setMember_id(member_id);
		aMapper.adminUpdateMemberEnabled(vo);
	}

	@Override
	public List<Map<String, Object>> adminCourseListData(int page) {
		
		int start = (page*10)-10;
		
		return aMapper.adminCourseListData(start);
	}

	@Override
	public int adminGetCountCourse() {
		
		return aMapper.adminGetCountCourse();
	}
	
	@Override
	public int adminGetCountFindCourse(String title) {
		
		return aMapper.adminGetCountFindCourse(title);
	}

	@Override
	public List<Map<String, Object>> adminFindCourseListData(String title, int page) {
		
		int start = (page*10)-10;
		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("start", start);
		
		return aMapper.adminFindCourseListData(map);
	}

	@Override
	public int adminGetTotalInstCount() {
		
		return aMapper.adminGetTotalInstCount();
	}

	@Override
	public List<CourseVO> adminGetBest5Course() {
		
		return aMapper.adminGetBest5Course();
	}

	@Override
	public List<Map<String, Object>> adminCoursePaymentListData(int page) {
		
		int start = (page*10)-10;
		return aMapper.adminCoursePaymentListData(start);
	}

	@Override
	public int adminCountCoursePayment() {
		
		return aMapper.adminCountCoursePayment();
	}

	@Override
	public List<Map<String, Object>> adminExamListData(int page) {
		
		int start = (page*10)-10;
		
		return aMapper.adminExamListData(start);
	}

	@Override
	public int adminExamCount() {
		
		return aMapper.adminExamCount();
	}

	@Override
	public List<Map<String, Object>> adminQnaListData(int page, int categoryno, String status) {
		
		Map<String, Object> map = new HashMap<>();
		int start = (page*10)-10;
		
		map.put("start", start);
		map.put("categoryno", categoryno);
		map.put("status", status);
		
		return aMapper.adminQnaListData(map);
	}

	@Override
	public int adminQnaCount(int categoryno, String status) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("categoryno", categoryno);
		map.put("status", status);
		
		return aMapper.adminQnaCount(map);
	}

	@Override
	public Map<String, Object> adminQnaDetailData(int no) {
		
		return aMapper.adminQnaDetailData(no);
	}

	@Override
	@Transactional
	public void adminQnaAnswerInsert(int member_id, int no, String content) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("member_id", member_id);
		map.put("no", no);
		map.put("content", content);
		
		aMapper.adminQnaAnswerInsert(map);
		aMapper.adminQnaStatusUpdate(no);
	}

	

	


}
