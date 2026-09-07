package com.sist.web.domain.mypage.vo;

import java.util.Date;

import lombok.Data;

/*
NO        NOT NULL NUMBER 
MEMBER_ID NOT NULL NUMBER 
COURSE_NO NOT NULL NUMBER 
REGDATE   NOT NULL DATE
 */
@Data
public class CourseCartVO {
	private int no,member_no,course_no;
	private Date regdate;
	
	// JOIN 컬럼
	private String title; // 강의명
	private String instructor; // 강사명
	private String thumbnail; // 강의 썸네일
	private int pay_price; // 할인 가격
	private int regular_price; // 원가
}
