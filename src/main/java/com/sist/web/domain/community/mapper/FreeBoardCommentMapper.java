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
<<<<<<< HEAD
=======
	public void freeBoardCommentInsert(FreeCommentVO vo);
	public void freeBoardCommentUpdate(FreeCommentVO vo);
	public int freeBoardCommentDeleteCount(int no);
	public void freeBoardCommentHardDelete(int no);
	public void freeBoardCommentSoftDelete(int no);
	public void freeBoardCommentDeleteForBoardDelete(int board_no);
>>>>>>> origin/dev
}
