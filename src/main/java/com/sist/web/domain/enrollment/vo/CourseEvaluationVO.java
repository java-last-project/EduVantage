package com.sist.web.domain.enrollment.vo;

import java.util.Date;

import lombok.Data;

/*
NO         NOT NULL NUMBER 
MEMBER_ID           NUMBER 
COURSE_NO           NUMBER 
RATING     NOT NULL NUMBER 
REVIEW              CLOB   
LIKE_COUNT          NUMBER 
REGDATE             DATE
 */
@Data
public class CourseEvaluationVO {
	private int no,member_id,course_no,rating,like_count;
	private String review,dbday;
	private Date regdate;
	
	// JOIN 컬럼
	private String name; // 강의평 작성자
}
