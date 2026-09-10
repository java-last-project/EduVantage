package com.sist.web.domain.enrollment.vo;

import java.util.Date;

import lombok.Data;

/*
NO        NOT NULL NUMBER         
MEMBER_ID          NUMBER         
COURSE_NO          NUMBER         
SUBJECT   NOT NULL VARCHAR2(2000) 
CONTENT   NOT NULL CLOB           
STATUS             CHAR(1)        
HIT                NUMBER         
REGDATE            DATE
 */
@Data
public class CourseQnaVO {
	private int no,member_id,course_no,hit;
	private String subject,content,status,dbday;
	private Date regdate;
	
	// JOIN 컬럼
	private String name; // 작성자명
	
	// 추가 컬럼
	private int curpage; // 강의 qna 현재 페이지
}
