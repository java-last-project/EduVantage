package com.sist.web.domain.member.service;

import com.sist.web.domain.member.vo.MemberVO;

public interface MemberService {
	public MemberVO memberInfoData(String username);
	
	public int memberIdCheck(String username);
	
	public int memberInsertData(MemberVO vo);
	
	public int memberAuthInsert(String username);
}
