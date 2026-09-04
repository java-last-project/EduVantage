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
<<<<<<< HEAD
	private int no,member_id,hit;
=======
	private int no,hit,cCount;
	private Integer member_id;
>>>>>>> origin/dev
	private String subject,content,pwd,name,dbday;
	private LocalDateTime regdate;
}
