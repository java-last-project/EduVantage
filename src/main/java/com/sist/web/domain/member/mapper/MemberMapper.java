package com.sist.web.domain.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.member.vo.*;

import java.util.List;

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

	@Select("""
			SELECT m.member_id FROM member m
			LEFT JOIN authority a
			ON m.member_id=a.member_id
			WHERE a.authority IN ('ROLE_INSTRUCTOR', 'ROLE_USER')
			""")
	public List<Integer> getAllMemberIdsExcludeAdmin();
}

