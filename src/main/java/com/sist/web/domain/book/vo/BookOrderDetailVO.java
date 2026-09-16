package com.sist.web.domain.book.vo;

import lombok.Data;

/*
 *  NO            NOT NULL NUMBER 
	BOOK_ORDER_NO NOT NULL NUMBER 
	BOOK_NO       NOT NULL NUMBER 
	QUANTITY               NUMBER 
	PRICE                  NUMBER 
 */
@Data
public class BookOrderDetailVO {
	private int no, book_order_no, book_no, quantity, price;
	
	//JOIN 컬럼
	private String title;
	private String poster;
}
