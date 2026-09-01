package com.sist.web.domain.exam.vo;

import lombok.Data;

//NO          NOT NULL NUMBER         
//TITLE       NOT NULL VARCHAR2(4000) 
//IMAGE                VARCHAR2(2000) 
//ANSWER      NOT NULL VARCHAR2(2000) 
//SCORE       NOT NULL NUMBER         
//DESCRIPTION          CLOB           
//TYPE                 NUMBER      
@Data
public class ExamQuestionVO {
	private int no,score,type,theme;
	private String title,image,answer,description;
	private ExamOptionVO ovo;
}
