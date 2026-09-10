package com.sist.web.domain.book.vo;

import lombok.Data;
import java.util.*;

/*
 * NO        NOT NULL NUMBER 
BOOK_NO   NOT NULL NUMBER 
MEMBER_ID NOT NULL NUMBER 
REGDATE            DATE   

 */
@Data
public class BookLikeVO {
	private int no, book_no, member_id;
	private String dbday;
	private Date regdate;
}
