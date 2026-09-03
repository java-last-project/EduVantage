package com.sist.web.domain.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.member.vo.*;

@Mapper
@Repository
public interface MemberMapper {
	// 로그인
	@Select("SELECT * FROM member "
			+"WHERE username=#{username}")
	public MemberVO memberInfoData(String username);
	
	//회원가입
	public int memberIdCheck(String username);

	public int memberInsertData(MemberVO vo);

	public int memberAuthInsert(String username);
}

