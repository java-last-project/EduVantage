package com.sist.web.domain.book.vo;
/*
 * NO         NOT NULL NUMBER        
	BOOK_NO    NOT NULL NUMBER        
	MEMBER_ID  NOT NULL NUMBER        
	NAME       NOT NULL VARCHAR2(100) 
	MSG        NOT NULL CLOB          
	REGDATE             DATE          
	GROUP_ID            NUMBER        
	GROUP_STEP          NUMBER        
	GROUP_TAB           NUMBER        
	ROOT                NUMBER        
	DEPTH               NUMBER        

 */

import java.util.Date;

import lombok.Data;
@Data
public class BookCommentVO {
	private int no, book_no, member_id, group_id, group_step, group_tab, root, depth;
	private String name, msg, dbday;
	private Date regdate;
}
