package com.sist.web.domain.community.vo;
//NO        NOT NULL NUMBER 
//MEMBER_ID NOT NULL NUMBER 
//QNA_NO    NOT NULL NUMBER 
//CONTENT   NOT NULL CLOB   
//REGDATE   NOT NULL DATE   

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class QnaReplyVO {
	private Integer no,member_id,qnaNo;
	private String content,dbday,name;
	private LocalDateTime regdate;
}
