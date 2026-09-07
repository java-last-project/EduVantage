package com.sist.web.domain.book.service;

import java.util.List;
import java.util.Map;

import com.sist.web.domain.book.vo.BookVO;

public interface BookService {
	public List<BookVO> bookListData(Map map);
	
	public int[] bookTotalPage(int page, String category);
	
	public int bookTotalCount(String category);
	
	public BookVO bookDetailData (int no);
	
	public int bookFindCount(Map<String, Object> map);

	public List<BookVO> bookFindData(Map<String, Object> map);
	
}
