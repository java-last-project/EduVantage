package com.sist.web.domain.exam.service;

import java.util.*;

import com.sist.web.domain.exam.vo.ExamQuestionVO;

public interface ExamService {
	public List<ExamQuestionVO> examListData(Integer type,int count);
}
