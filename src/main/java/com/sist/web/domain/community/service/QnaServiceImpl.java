package com.sist.web.domain.community.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.sist.web.domain.community.mapper.QnaMapper;
import com.sist.web.domain.community.vo.QnaBoardVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaServiceImpl implements QnaService {
	private final QnaMapper qMapper;
	private final int ROW=20;
	private final int BLOCK=10;
	
	@Override
	public List<QnaBoardVO> qnaListData(int page, String fd, Integer category) {
		Map<String,Object> map=new HashMap<>();
		int start=(page*ROW)-ROW;
		map.put("start", start);
		if(fd!=null && !"".equals(fd)) {
			map.put("fd", fd);
		}
		if(category!=null && category!=0) {
			map.put("categoryNo", category);
		}
		return qMapper.qnaListData(map);
	}

	@Override
	public Map<String,Object> qnaPageData(int page, String fd, Integer category) {
		Map<String,Object> map=new HashMap<>();
		if(fd!=null && !"".equals(fd)) {
			map.put("fd", fd);
		}
		if(category!=null && category!=0) {
			map.put("categoryNo", category);
		}
		int count=qMapper.qnaListCount(map);
		int totalpage=(int)Math.ceil(count/(double)ROW);
		int startPage=((page-1)/BLOCK*BLOCK)+1;
		int endPage=((page-1)/BLOCK*BLOCK)+BLOCK;
		if(endPage>totalpage) endPage=totalpage;
		
		map.put("curpage", page);
		map.put("totalpage", totalpage);
		map.put("startPage", startPage);
		map.put("endPage", endPage);
		map.put("count", count);
		return map;
	}

	@Override
	public QnaBoardVO qnaDetail(int no) {
		return qMapper.qnaDetailData(no);
	}

	@Override
	public void qnaInsert(QnaBoardVO vo) {
		qMapper.qnaInsert(vo);
	}

	@Override
	public boolean qnaUpdate(QnaBoardVO vo) {
		boolean sCheck=false;
		String status=qMapper.qnaDeleteValidate(vo.getNo());
		if("N".equals(status)) {
			qMapper.qnaUpdate(vo);
			sCheck=true;
		}
		return sCheck;
	}

	@Override
	public boolean qnaDelete(int no) {
		boolean sCheck=false;
		String status=qMapper.qnaDeleteValidate(no);
		if("N".equals(status)) {
			qMapper.qnaDelete(no);
			sCheck=true;
		}
		return sCheck;
	}

}
