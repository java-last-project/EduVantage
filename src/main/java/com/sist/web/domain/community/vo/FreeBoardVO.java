package com.sist.web.domain.community.vo;
//NO        NOT NULL NUMBER         
//MEMBER_ID          NUMBER         
//SUBJECT   NOT NULL VARCHAR2(2000) 
//CONTENT   NOT NULL CLOB           
//HIT                NUMBER         
//PWD                VARCHAR2(100)  
//REGDATE   NOT NULL DATE           
//NAME               VARCHAR2(50)   



import java.time.LocalDateTime;

import lombok.Data;
@Data
public class FreeBoardVO {
	private int no,hit,cCount;
	private Integer member_id;
	private String subject,content,pwd,name,dbday;
	private LocalDateTime regdate;
}
