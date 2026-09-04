package com.sist.web.domain.book.vo;
import java.util.*;

import lombok.Data;
/*
 *  NO            NOT NULL NUMBER        
	TITLE         NOT NULL VARCHAR2(500) 
	POSTER                 VARCHAR2(500) 
	AUTHOR        NOT NULL VARCHAR2(50)  
	PUBDATE                DATE          
	PAGES                  VARCHAR2(10)  
	ISBN          NOT NULL VARCHAR2(15)  
	PRICE         NOT NULL NUMBER        
	BOOK_DESC              CLOB          
	DESC_IMG               VARCHAR2(500) 
	AUTHOR_DESC            CLOB          
	TOC                    CLOB          
	CATEGORY               VARCHAR2(51)  
	STOCK         NOT NULL NUMBER        
	HIT                    NUMBER        
	LIKE_COUNT             NUMBER        
	COMMENT_COUNT          NUMBER    
 */
@Data
public class BookVO {
	private int no, price, stock, hit, like_count, comment_count;
	private String title, poster, author, pages, isbn, book_desc, desc_img, author_desc, toc, category, dbday, publisher;
	private Date pubdate; 
}
