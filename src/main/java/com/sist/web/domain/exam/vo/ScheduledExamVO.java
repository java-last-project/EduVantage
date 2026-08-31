package com.sist.web.domain.exam.vo;

import java.time.LocalDateTime;

import lombok.Data;

//NO         NOT NULL NUMBER         
//TITLE      NOT NULL VARCHAR2(1000) 
//OPEN_DATE  NOT NULL TIMESTAMP(6)   
//CLOSE_DATE NOT NULL TIMESTAMP(6) 
@Data
public class ScheduledExamVO {
	private int no;
	private String title;
	private LocalDateTime open_date,close_date;
}
