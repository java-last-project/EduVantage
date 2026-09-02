package com.sist.web.domain.course.vo;

import lombok.Data;

import java.time.LocalDateTime;
//NO           NOT NULL NUMBER    
//MEMBER_ID    NOT NULL NUMBER    
//COURSE_NO    NOT NULL NUMBER    
//IS_COMPLETED          CHAR(1)   
//PROGRESS              NUMBER(3) 
//REGDATE               DATE     
@Data
public class CourseEnrollmentVO {
	private int no,member_id,course_no,progress;
	private String is_completed;
	private LocalDateTime regdate;
	private CourseVO cvo;
}
