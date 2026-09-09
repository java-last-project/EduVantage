package com.sist.web.domain.exam.service;

import java.util.*;

import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;

public interface ExamService {
	public List<ExamQuestionVO> examDetailData(Integer examNo,Integer theme,int count);
	public Integer insertEnrollment(ExamEnrollmentVO vo);
	public String getExamTitle(Integer examNo);
}
