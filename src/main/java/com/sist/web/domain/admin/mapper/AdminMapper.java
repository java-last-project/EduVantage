package com.sist.web.domain.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.member.vo.MemberVO;

import java.util.*;

@Mapper
@Repository
public interface AdminMapper 
{
	// 회원 리스트 조회
	/*
	 * 	<select id="adminMemberListData" resultType="hashmap" parameterType="int">
			SELECT m.member_id,m.username,m.name,TO_CHAR(m.regdate,'yyyy-mm-dd') as dbday,a.authority,m.enabled
			FROM member m
			JOIN authority a
			ON m.member_id=a.member_id
			ORDER BY m.member_id ASC
			OFFSET #{start} ROWS FETCH NEXT 10 ROWS ONLY
		</select>
	 */
	public List<Map<String,Object>> adminMemberListData(int start);
	
	//총페이지
	@Select("SELECT COUNT(*) FROM member")
	public int getCountMember();
	
	// 이름으로 검색
	/*
	 * 	<select id="adminMemberFindName" resultType="hashmap" parameterType="hashmap">
			SELECT m.member_id,m.username,m.name,TO_CHAR(m.regdate,'yyyy-mm-dd') as dbday,a.authority,m.enabled
			FROM member m
			JOIN authority a
			ON m.member_id=a.member_id
			WHERE m.name=#{name}
			ORDER BY m.member_id ASC
			OFFSET #{start} ROWS FETCH NEXT 10 ROWS ONLY
		</select>
	 */
	public List<Map<String,Object>> adminMemberFindByName(String name);
	
	// 조건 필터링된 데이터 조회
	/*
	 * <select id="adminMemberFilterListData" resultType="hashmap" parameterType="int">
			SELECT m.member_id,m.username,m.name,TO_CHAR(m.regdate,'yyyy-mm-dd') as dbday,a.authority,m.enabled
			FROM member m
			JOIN authority a
			ON m.member_id=a.member_id
			
			<where>
				<if test="enabled!=-1">
					AND enabled=#{enabled}
				</if>
				<if test="authority!='all'">
					AND authority=#{authority}
				</if>
			</where>
			
			ORDER BY m.member_id ASC
			OFFSET #{start} ROWS FETCH NEXT 10 ROWS ONLY
		</select>
	 */
	public List<Map<String,Object>> adminMemberFilterListData(Map<String, Object> map);
	
	//필터링 된 총페이지
	/*
	 * 	<select id="getCountFilterMember" resultType="int" parameterType="hashmap">
			SELECT COUNT(*)
			FROM member m
			JOIN authority a
			ON m.member_id=a.member_id
			<where>
				<if test="authority!='all'">
					AND authority=#{authority}
				</if>
				<if test="enabled!=-1">
					AND enabled=#{enabled}
				</if>
			</where>
		</select>
	 */
	public int getCountFilterMember(Map<String, Object> map);
}
