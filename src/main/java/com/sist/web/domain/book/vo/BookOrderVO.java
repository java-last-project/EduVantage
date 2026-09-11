package com.sist.web.domain.book.vo;
/*
 *  NO           NOT NULL NUMBER       
	MEMBER_ID    NOT NULL NUMBER       
	TOTAL_PRICE  NOT NULL NUMBER       
	ORDER_STATUS          VARCHAR2(20) 
	REGDATE               DATE         

 */

import lombok.Data;
import java.util.*;

@Data
public class BookOrderVO {
	private int no, member_id, total_price;
	private String order_status, dbday;
	private Date regdate;
	
	// 자식 리스트
	private List<BookOrderDetailVO> detailList;
}