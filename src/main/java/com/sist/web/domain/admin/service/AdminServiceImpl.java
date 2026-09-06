package com.sist.web.domain.admin.service;

import java.util.*;
import com.sist.web.domain.member.vo.*;

import org.springframework.stereotype.Service;

import com.sist.web.domain.admin.mapper.AdminMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService
{
	private final AdminMapper aMapper;

	@Override
	public int[] getPageData(int page) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		int start = (page*10)-10;
		
		return aMapper.adminMemberListData(start);
	}

	@Override
	public int getTotalMember() {
		// TODO Auto-generated method stub
		return aMapper.getCountMember();
	}

	@Override
	public List<Map<String,Object>> adminMemberFindByName(String name) {
		// TODO Auto-generated method stub
		return aMapper.adminMemberFindByName(name);
	}

	@Override
	public List<Map<String, Object>> adminMemberFilterListData(String authority, String enabled, int page) {
		// TODO Auto-generated method stub
		int start = (page*10)-10;
		Map<String, Object> map = new HashMap<>();
		map.put("authority", authority);
		map.put("enabled", enabled);
		map.put("start", start);
		return aMapper.adminMemberFilterListData(map);
	}

	@Override
	public int getCountFilterMember(String authority, String enabled) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("authority", authority);
		map.put("enabled", Integer.parseInt(enabled));
		return aMapper.getCountFilterMember(map);
	}

	@Override
	public Map<String, Object> adminMemberDetailData(int member_id) {
		// TODO Auto-generated method stub
		return aMapper.adminMemberDetailData(member_id);
	}

	@Override
	public void adminUpdateMemberEnabled(int enabled, int member_id) {
		// TODO Auto-generated method stub
		MemberVO vo = new MemberVO();
		vo.setEnabled(enabled);
		vo.setMember_id(member_id);
		aMapper.adminUpdateMemberEnabled(vo);
	}

	


}
