package com.sist.web.domain.community.vo;
//NO        NOT NULL NUMBER        
//BOARD_NO           NUMBER        
//MEMBER_ID          NUMBER        
//NAME               VARCHAR2(100) 
//MSG       NOT NULL CLOB          
//REGDATE            DATE          
//PWD                VARCHAR2(100) 
//PARENT_NO          NUMBER     



import java.time.LocalDateTime;

import lombok.Data;
@Data
public class FreeCommentVO {
	private int no,board_no,member_id,parent_no,group_tab;
	private String name,msg,pwd,dbday;
	private LocalDateTime regdate;
}
