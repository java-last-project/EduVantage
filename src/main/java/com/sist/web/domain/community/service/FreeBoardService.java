package com.sist.web.domain.community.service;

import java.util.*;import com.sist.web.domain.community.mapper.FreeBoardCommentMapper;
import com.sist.web.domain.community.vo.FreeBoardVO;
import com.sist.web.domain.community.vo.FreeCommentVO;

public interface FreeBoardService {
	public List<FreeBoardVO> freeBoardList(int page,String fd);
	public int[] freeBoardPageData(int page,String fd);
	public FreeBoardVO freeBoardDetail(int no);
	public List<FreeCommentVO> freeCommentList(int no);
	public int freeCommentCount(int no);
}
