package com.sist.web.domain.enrollment.vo;

import java.util.Date;

import com.sist.web.domain.course.vo.CourseVO;

import lombok.Data;

/*
NO            NOT NULL NUMBER    
MEMBER_ID     NOT NULL NUMBER    
COURSE_NO     NOT NULL NUMBER    
IS_COMPLETED           CHAR(1)   
PROGRESS               NUMBER(3) 
REGDATE                DATE       => 수강 등록일
LAST_ACCESSED          DATE       => 강의 학습 페이지 최근 접속일
 */
@Data
public class CourseEnrollmentVO {
	private int no,member_id,course_no,progress;
	private String is_completed;
	private Date regdate,last_accessed;
	private String dbday; // 수강 등록일 형변환
	
	// JOIN 전용 컬럼
	private CourseVO cvo;	
	private String title; // 강의명
	private String instructor; // 강사명
	private String thumbnail; // 강의 썸네일 이미지
}
