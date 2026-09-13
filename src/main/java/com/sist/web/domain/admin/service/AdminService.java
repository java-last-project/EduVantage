package com.sist.web.domain.admin.service;

import java.util.*;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.member.vo.MemberVO;

public interface AdminService {
	
	// 회원 관리
	public List<Map<String,Object>> adminMemberListData(int page);
	
	// 오버로딩해서 회원수 데이터를 따로 구하게 만들기 (필터링 없는 버전, 필터링 있는 버전)
	public int[] getPageData(int page);
	public int[] getPageData(int page, String authority, String enabled);
	
	public int getTotalMember();
	
	public List<Map<String,Object>> adminMemberFindByName(String name);	// 이름으로 검색
	
	public List<Map<String,Object>> adminMemberFilterListData(String authority, String enabled, int page);
	public int getCountFilterMember(String authority, String enabled);
	
	public Map<String, Object> adminMemberDetailData(int member_id);	// 회원 상세 정보 조회
	
	public void adminUpdateMemberEnabled(int enabled, int member_id);
	
	// 강의 관리
	public List<Map<String, Object>> adminCourseListData(int page);	// 전체 강의 목록 조회
	
	public int adminGetCountCourse();					// 전체 강의 총 갯수 얻기
	public int adminGetCountFindCourse(String title);	// 강의명으로 검색한 강의 총 갯수 얻기
	
	public List<Map<String, Object>> adminFindCourseListData(String title, int page);	// 강의명으로 검색
	
	// 대시보드 출력용
	public int adminGetTotalInstCount();	// 총 강사수
	public List<CourseVO> adminGetBest5Course();	// 수강생 수 Best 5 강좌
	
}
