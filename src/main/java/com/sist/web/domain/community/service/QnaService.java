package com.sist.web.domain.community.service;

import java.util.*;

import com.sist.web.domain.community.vo.QnaBoardVO;

public interface QnaService {
	public List<QnaBoardVO> qnaListData(int page,String fd,Integer category);
	public Map<String,Object> qnaPageData(int page,String fd,Integer category);
	public QnaBoardVO qnaDetail(int no);
	public void qnaInsert(QnaBoardVO vo);
	public boolean qnaUpdate(QnaBoardVO vo);
	public boolean qnaDelete(int no);
}
