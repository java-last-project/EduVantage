package com.sist.web.domain.exam.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;

import com.sist.web.domain.exam.mapper.ExamMapper;
import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService{
	private final ExamMapper eMapper;
	
	@Override
	public List<ExamQuestionVO> examDetailData(Integer theme,int count) {
		Map<String, Object> map=new HashMap<>();
		if(theme!=null&&theme!=0) {
			map.put("theme", theme);
		}
		map.put("count", count);
		List<ExamQuestionVO> list=eMapper.examDetailData(map);
		return list;
	}

	@Override
	@Transactional
	public Integer insertEnrollment(ExamEnrollmentVO vo) {
		vo.setStarttime(LocalDateTime.now());
		eMapper.insertEnrollment(vo);
		return vo.getNo();
	}

}
