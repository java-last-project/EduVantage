package com.sist.web.domain.book.vo;
import java.util.*;

import lombok.Data;
/*
 *  NO        NOT NULL NUMBER 
	MEMBER_ID NOT NULL NUMBER 
	BOOK_NO   NOT NULL NUMBER 
	QUANTITY  NOT NULL NUMBER 
	REGDATE            DATE   
 */
@Data
public class BookCartVO {
	private int no, member_id, book_no, quantity;
	private Date regdate;
	// JOIN용 변수
	private String title;
    private String poster;
    private int price;
}
