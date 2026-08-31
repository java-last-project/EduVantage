package com.sist.web.domain.exam.vo;

import lombok.Data;

//NO          NOT NULL NUMBER 
//EXAM_NO     NOT NULL NUMBER 
//QUESTION_NO NOT NULL NUMBER 
@Data
public class ScheduledExamMapVO {
	private int no,exam_no,question_no;
	// 조회용
	private ExamQuestionVO qvo;
}
