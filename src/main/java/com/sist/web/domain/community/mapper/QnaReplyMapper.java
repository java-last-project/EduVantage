package com.sist.web.domain.community.mapper;

import com.sist.web.domain.community.vo.QnaBoardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.community.vo.QnaReplyVO;

import java.util.*;

@Mapper
@Repository
public interface QnaReplyMapper {
	
	public QnaReplyVO qnaReplyData(int qnaNo);
	public void qnaStatusUpdate(Map<String,Object> map);
	public void qnaReplyInsert(QnaReplyVO vo);
	public void qnaReplyUpdate(QnaReplyVO vo);
	public void qnaReplyDelete(int no);

	@Select("""
			SELECT member_id, subject
			FROM qnaboard
			WHERE no=#{no}
			""")
	public QnaBoardVO qnaInfo(int no);
}
