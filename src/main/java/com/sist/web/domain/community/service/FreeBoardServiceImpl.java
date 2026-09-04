package com.sist.web.domain.community.service;

import java.util.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sist.web.domain.community.mapper.FreeBoardCommentMapper;
import com.sist.web.domain.community.mapper.FreeBoardMapper;
import com.sist.web.domain.community.vo.FreeBoardVO;
import com.sist.web.domain.community.vo.FreeCommentVO;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService {
	private final FreeBoardMapper fMapper;
	private final FreeBoardCommentMapper cMapper;
	private final int ROW=20;
	private final BCryptPasswordEncoder passwordEncoder;

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
	@Transactional
	public FreeBoardVO freeBoardDetail(int no) {
		fMapper.freeBoardHitIncrement(no);
		return fMapper.freeBoardDetail(no);
	}

	@Override
	public void freeBoardInsert(FreeBoardVO vo, HttpSession session) {
		Integer memberId=(Integer)session.getAttribute("member_id");
		if(memberId!=null){
			vo.setMember_id(memberId);
			vo.setName(null);
			vo.setPwd(null);
		}else{
			if(vo.getPwd()!=null && !vo.getPwd().isEmpty()){
				vo.setPwd(passwordEncoder.encode(vo.getPwd()));
			}
		}
		fMapper.freeBoardInsert(vo);
	}

	@Override
	public FreeBoardVO freeBoardUpdateData(int no){
		return fMapper.freeBoardDetail(no);
	}

	@Override
	public void freeBoardUpdate(FreeBoardVO vo) {
		fMapper.freeBoardUpdate(vo);
	}

	@Override
	@Transactional
	public void freeBoardDelete(int no) {
		cMapper.freeBoardCommentDeleteForBoardDelete(no);
		fMapper.freeBoardDelete(no);
	}

	@Override
	public boolean freeBoardPwdCheck(int no, String pwd) {
		boolean bCheck;
		String dbPwd=fMapper.freeBoardPwdData(no);
		bCheck=passwordEncoder.matches(pwd,dbPwd);
		return bCheck;
	}

	@Override
	public List<FreeCommentVO> freeCommentList(int no) {
		return cMapper.freeBoardCommentList(no);
	}

	@Override
	public int[] freeCommentCount(int no,int page) {
		int count= cMapper.freeBoardCommentCount(no);
		int totalpage=(int)Math.ceil(count/(double)ROW);
		final int BLOCK=10;
		int startPage=((page-1)/BLOCK*BLOCK)+1;
		int endPage=((page-1)/BLOCK*BLOCK)+BLOCK;
		if(endPage>totalpage) endPage=totalpage;
		int[] pages= {page,totalpage,startPage,endPage,count};
		return pages;
	}

	@Override
	public void freeBoardCommentInsert(FreeCommentVO vo) {
		cMapper.freeBoardCommentInsert(vo);
	}

	@Override
	public void freeBoardCommentUpdate(int no, String msg) {
		FreeCommentVO vo=new FreeCommentVO();
		vo.setNo(no);
		vo.setMsg(msg);
		cMapper.freeBoardCommentUpdate(vo);
	}

	@Override
	@Transactional
	public void freeBoardCommentDelete(int no) {
		int childCount=cMapper.freeBoardCommentDeleteCount(no);
		if(childCount>0){
			cMapper.freeBoardCommentSoftDelete(no);
		}else{
			cMapper.freeBoardCommentHardDelete(no);
		}
	}

}
