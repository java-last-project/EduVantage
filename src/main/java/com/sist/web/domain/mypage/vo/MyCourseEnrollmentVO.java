package com.sist.web.domain.mypage.vo;

import lombok.Data;
import java.util.*;
/*
NO           NOT NULL NUMBER    
MEMBER_ID    NOT NULL NUMBER    
COURSE_NO    NOT NULL NUMBER    
IS_COMPLETED          CHAR(1)   
PROGRESS              NUMBER(3) 
REGDATE               DATE
 */
@Data
public class MyCourseEnrollmentVO {
	private int no,member_id,course_no,progress;
	private String is_completed,dbday;
	private Date regdate,last_accessed;
	
	private String title,instructor,thumbnail;
}
