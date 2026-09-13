package com.sist.web.domain.enrollment.vo;

import lombok.Data;

/*
NO        NOT NULL NUMBER 
CE_NO              NUMBER 
MEMBER_ID          NUMBER 
REGDATE            DATE
 */
@Data
public class CourseEvaluationLikeVO {
	private int no,ce_no,member_id;
	
	// 추가 컬럼
	private int course_no;
	private int curpage;
}
