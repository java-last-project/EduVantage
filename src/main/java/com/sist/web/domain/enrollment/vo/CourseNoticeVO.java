package com.sist.web.domain.enrollment.vo;

import lombok.Data;
import java.util.*;
/*
NO        NOT NULL NUMBER         
COURSE_ID NOT NULL NUMBER         
SUBJECT   NOT NULL VARCHAR2(2000) 
CONTENT   NOT NULL CLOB           
REGDATE   NOT NULL DATE           
HIT       NOT NULL NUMBER         
FILENAME           VARCHAR2(1000) 
FILESIZE           NUMBER
 */
@Data
public class CourseNoticeVO {
	private int no,course_id,hit,filesize;
	private String subject,content,dbday,filename;
	private Date regdate;
	
	// JOIN 컬럼
	private String instuctor; // 강사명
}
