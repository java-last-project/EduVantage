package com.sist.web.domain.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.community.vo.FreeCommentVO;

import java.util.*;

@Mapper
@Repository
public interface FreeBoardCommentMapper {
	public List<FreeCommentVO> freeBoardCommentList(int no);
	public int freeBoardCommentCount(int no);
	public void freeBoardCommentInsert(FreeCommentVO vo);
	public void freeBoardCommentUpdate(FreeCommentVO vo);
	public int freeBoardCommentDeleteCount(int no);
	public void freeBoardCommentHardDelete(int no);
	public void freeBoardCommentSoftDelete(int no);
	public void freeBoardCommentDeleteForBoardDelete(int board_no);
	//대댓글 알림용: 부모 댓글 정보
	@Select("""
		SELECT *
		FROM free_comment
		WHERE no=#{parentNo}
	""")
	public FreeCommentVO parentFeeBoardCommentInfo(int parentNo);
}
