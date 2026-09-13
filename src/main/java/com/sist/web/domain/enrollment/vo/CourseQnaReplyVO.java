package com.sist.web.domain.enrollment.vo;
/*
NO            NOT NULL NUMBER 
COURSE_QNA_NO          NUMBER 
MEMBER_ID              NUMBER 
ANSWER        NOT NULL CLOB   
REGDATE                DATE
 */

import java.util.Date;

import lombok.Data;

@Data
public class CourseQnaReplyVO {
	private int no,course_qna_no,member_id;
	private String answer,dbday;
	private Date regdate;
	
	// JOIN 컬럼
	private String name;
}
