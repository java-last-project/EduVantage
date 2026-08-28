package com.sist.web.domain.exam.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.sist.web.domain.exam.mapper.ExamMapper;
import com.sist.web.domain.exam.vo.ExamQuestionVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService{
	private final ExamMapper eMapper;
	
	@Override
	public List<ExamQuestionVO> examListData(Integer type,int count) {
		Map<String, Object> map=new HashMap<>();
		if(type!=null&&type!=0) {
			map.put("type", type);
		}
		map.put("count", count);
		List<ExamQuestionVO> list=eMapper.examListData(map);
		return list;
	}

}
