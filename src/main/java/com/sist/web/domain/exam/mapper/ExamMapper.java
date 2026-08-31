package com.sist.web.domain.exam.mapper;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.exam.vo.ExamQuestionVO;

@Mapper
@Repository
public interface ExamMapper {
	
	public List<ExamQuestionVO> examListData(Map<String,Object> map);
}
