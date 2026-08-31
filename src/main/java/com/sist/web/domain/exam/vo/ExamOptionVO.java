package com.sist.web.domain.exam.vo;

import lombok.Data;

//NO          NOT NULL NUMBER         
//QUESTION_NO NOT NULL NUMBER         
//OPTION1              VARCHAR2(2000) 
//OPTION2              VARCHAR2(2000) 
//OPTION3              VARCHAR2(2000) 
//OPTION4              VARCHAR2(2000) 
@Data
public class ExamOptionVO {
	private int no,question_no;
	private String option1,option2,option3,option4;
}
