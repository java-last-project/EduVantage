package com.sist.web.domain.community.service;

import java.util.*;import com.sist.web.domain.community.mapper.FreeBoardCommentMapper;
import com.sist.web.domain.community.vo.FreeBoardVO;
import com.sist.web.domain.community.vo.FreeCommentVO;
import jakarta.servlet.http.HttpSession;


public interface FreeBoardService {
	public List<FreeBoardVO> freeBoardList(int page,String fd);
	public int[] freeBoardPageData(int page,String fd);
	public FreeBoardVO freeBoardDetail(int no);
	public void freeBoardInsert(FreeBoardVO vo, HttpSession session);
	public FreeBoardVO freeBoardUpdateData(int no);
	public void freeBoardUpdate(FreeBoardVO vo);
	public void freeBoardDelete(int no);
	public boolean freeBoardPwdCheck(int no,String pwd);

	public List<FreeCommentVO> freeCommentList(int no);
	public int[] freeCommentCount(int no,int page);
	public void freeBoardCommentInsert(FreeCommentVO vo);
	public void freeBoardCommentUpdate(int no,String msg);
	public void freeBoardCommentDelete(int no);
}
