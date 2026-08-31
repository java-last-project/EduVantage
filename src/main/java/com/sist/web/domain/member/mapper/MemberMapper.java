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
	/*
	 * <select id="memberIdCheck" resultType="com.sist.web.domain.vo.MemberVO" parameterType="string">
		SELECT member_id, username
		FROM member
		WHERE  username=#{username}
	</select>
	 */
	public MemberVO memberIdCheck(String username);
}

