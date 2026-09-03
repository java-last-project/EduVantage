package com.sist.web.domain.admin.service;

import java.util.*;

import com.sist.web.domain.member.vo.MemberVO;

public interface AdminService {
	//public List<MemberVO> adminMemberListData(int page);
	public List<Map<String,Object>> adminMemberListData(int page);
	public int[] getPageData(int page);
	public int getTotalMember();
	
	public List<Map<String,Object>> adminMemberFindByName(String name);	// 이름으로 검색
	
	public List<Map<String,Object>> adminMemberFilterListData(String authority, String enabled, int page);
	public int getCountFilterMember(String authority, String enabled);
	public int[] getPageData(int page, String authority, String enabled);
}
