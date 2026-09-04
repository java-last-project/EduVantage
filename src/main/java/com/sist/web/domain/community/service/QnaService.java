package com.sist.web.domain.community.service;

import java.util.*;

import com.sist.web.domain.community.vo.QnaBoardVO;
import com.sist.web.domain.community.vo.QnaReplyVO;

public interface QnaService {
	public List<QnaBoardVO> qnaListData(int page,String fd,Integer category);
	public Map<String,Object> qnaPageData(int page,String fd,Integer category);
	public QnaBoardVO qnaDetail(int no);
	public void qnaInsert(QnaBoardVO vo);
	public boolean qnaUpdate(QnaBoardVO vo);
	public boolean qnaDelete(int no);
	
	public QnaReplyVO qnaReplyData(Integer qnaNo);
	public void qnaReplyInsert(QnaReplyVO vo,Integer qnaNo);
	public void qnaReplyUpdate(QnaReplyVO vo,Integer qnaNo);
	public void qnaReplyDelete(int no,Integer qnaNo);
}
