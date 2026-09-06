package com.sist.web.domain.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.community.vo.QnaBoardVO;

import java.util.*;

@Mapper
@Repository
public interface QnaMapper {
	
	public List<QnaBoardVO> qnaListData(Map<String,Object> map);
	public int qnaListCount(Map<String,Object> map);
	public QnaBoardVO qnaDetailData(int no);
	public void qnaInsert(QnaBoardVO vo);
	public void qnaUpdate(QnaBoardVO vo);
	public String qnaDeleteValidate(int no);
	public void qnaDelete(int no);
}
