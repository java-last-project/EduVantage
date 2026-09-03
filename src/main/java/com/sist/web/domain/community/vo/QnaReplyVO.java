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
	private int no,member_id,qna_no;
	private String content,dbday;
	private LocalDateTime regdate;
}
