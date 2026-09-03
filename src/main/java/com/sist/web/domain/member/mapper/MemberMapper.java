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
	public int memberIdCheck(String username);
	
	/*
	 * <insert id="memberInsertData" parameterType="com.sist.web.domain.member.vo.MemberVO">
	    INSERT INTO member (member_id, username, password, name, sex, birthdate, 
	    phone, post, addr1, addr2, profile_desc
	    ) 
	    VALUES (member_seq.NEXTVAL, #{username}, #{password}, #{name}, #{sex}, 
	    TO_DATE(#{birthdate}, 'YYYYMMDD'), #{phone}, #{post}, #{addr1}, #{addr2}, #{profileDesc}
	    )
	</insert>
	 */
	public int memberInsertData(MemberVO vo);
	/*
	 * <insert id="memberAuthInsert" parameterType="string">
	    INSERT INTO member_auth (no, member_id, authority) 
	    VALUES (
	        AUTH_ANO_SEQ.NEXTVAL, 
	        (SELECT member_id FROM member WHERE username = #{username}), 
	        'ROLE_USER'
	    )
	</insert>
	 */
	public int memberAuthInsert(String username);
}

