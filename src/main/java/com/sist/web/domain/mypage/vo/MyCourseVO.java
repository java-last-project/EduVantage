package com.sist.web.domain.mypage.vo;

import lombok.Data;

/*
NO            NOT NULL NUMBER         
TITLE         NOT NULL VARCHAR2(1000) 
INSTRUCTOR_NO NOT NULL NUMBER         
STAR                   NUMBER(3,1)    
STUDENT_COUNT          NUMBER         
PAY_PRICE              NUMBER         
REGULAR_PRICE          NUMBER         
CONTENT                CLOB           
IMAGES                 CLOB           
THUMBNAIL              VARCHAR2(1000)
 */

@Data
public class MyCourseVO {
	private int no,instructor_no;
	private String title,thumbnail;
}
