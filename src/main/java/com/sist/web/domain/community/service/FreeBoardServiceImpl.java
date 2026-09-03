package com.sist.web.domain.community.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.sist.web.domain.community.mapper.FreeBoardCommentMapper;
import com.sist.web.domain.community.mapper.FreeBoardMapper;
import com.sist.web.domain.community.vo.FreeBoardVO;
import com.sist.web.domain.community.vo.FreeCommentVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService {
	private final FreeBoardMapper fMapper;
	private final FreeBoardCommentMapper cMapper;
	private final int ROW=20;
	
	@Override
	public List<FreeBoardVO> freeBoardList(int page,String fd) {
		Map<String,Object> map=new HashMap<>();
		int start=(page*ROW)-ROW;
		map.put("start", start);
		if(fd!=null && !"".equals(fd)) {
			map.put("fd", fd);
		}
		return fMapper.freeBoardList(map);
	}

	@Override
	public int[] freeBoardPageData(int page,String fd) {
		Map<String,Object> map=new HashMap<>();
		if(fd!=null && !"".equals(fd)) {
			map.put("fd", fd);
		}
		int count=fMapper.freeBoardCount(map);
		int totalpage=(int)Math.ceil(count/(double)ROW);
		final int BLOCK=10;
		int startPage=((page-1)/BLOCK*BLOCK)+1;
		int endPage=((page-1)/BLOCK*BLOCK)+BLOCK;
		if(endPage>totalpage) endPage=totalpage;
		int[] pages= {page,totalpage,startPage,endPage,count};
		return pages;
	}

	@Override
	public FreeBoardVO freeBoardDetail(int no) {
		return fMapper.freeBoardDetail(no);
	}

	@Override
	public List<FreeCommentVO> freeCommentList(int no) {
		return cMapper.freeBoardCommentList(no);
	}

	@Override
	public int freeCommentCount(int no) {
		return cMapper.freeBoardCommentCount(no);
	}

}
