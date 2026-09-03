package com.sist.web.domain.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.sist.web.domain.community.vo.FreeBoardVO;

import java.util.*;
@Mapper
@Repository
public interface FreeBoardMapper {
	
	public List<FreeBoardVO> freeBoardList(Map<String,Object> map);
	public int freeBoardCount(Map<String,Object> map);
	
	@Select("""
			SELECT b.no,b.member_id,b.hit,b.subject,NVL(m.name, b.name) AS name,TO_CHAR(b.regdate,'yyyy-mm-dd HH24:mi:ss') AS dbday,b.content
			FROM free_board b
			LEFT JOIN member m ON b.member_id=m.member_id
			WHERE b.no=#{no}
			""")
	public FreeBoardVO freeBoardDetail(int no);
}
