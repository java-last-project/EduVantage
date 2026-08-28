package com.sist.web.domain.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.member.vo.*;

@Mapper
@Repository
public interface MemberMapper {
	@Select("SELECT * FROM member "
			+"WHERE username=#{username}")
	public MemberVO memberInfoData(String username);
}

