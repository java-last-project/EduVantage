package com.sist.web.domain.mypage.vo;
/*
NO        NOT NULL NUMBER 
MEMBER_ID NOT NULL NUMBER 
COURSE_NO NOT NULL NUMBER 
PRICE              NUMBER 
REGDATE   NOT NULL DATE
 */

import java.util.Date;

import lombok.Data;

@Data
public class CoursePaymentVO {
	private int no,member_id,course_no,price;
	private Date regdate;
	private String dbday;
	
	// JOIN 컬럼
	private String title; // 강의명
	private String instructor; // 강사명
	private String thumbnail; // 강의 썸네일
}
