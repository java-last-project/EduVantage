package com.sist.web.domain.community.vo;
//NO        NOT NULL NUMBER         
//MEMBER_ID NOT NULL NUMBER         
//SUBJECT   NOT NULL VARCHAR2(2000) 
//CONTENT   NOT NULL CLOB           
//REGDATE   NOT NULL DATE           
//STATUS    NOT NULL CHAR(1)    

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class QnaBoardVO {
	private int no,member_id,category;
	private String subject,content,status,dbday;
	private LocalDateTime regdate;
}
