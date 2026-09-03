package com.sist.web.domain.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.community.vo.FreeCommentVO;

import java.util.*;

@Mapper
@Repository
public interface FreeBoardCommentMapper {
	public List<FreeCommentVO> freeBoardCommentList(int no);
	public int freeBoardCommentCount(int no);
}
