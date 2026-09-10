package com.sist.web.domain.book.service;

import java.util.List;
import java.util.Map;

import com.sist.web.domain.book.vo.BookCartVO;
import com.sist.web.domain.book.vo.BookLikeVO;
import com.sist.web.domain.book.vo.BookOrderDetailVO;
import com.sist.web.domain.book.vo.BookOrderVO;
import com.sist.web.domain.book.vo.BookVO;

public interface BookService {
	public List<BookVO> bookListData(Map map);
	
	public int bookTotalCount(String category);
	
	public BookVO bookDetailData (int no);
	
	public int bookFindCount(Map<String, Object> map);

	public List<BookVO> bookFindData(Map<String, Object> map);
	
	public int bookLikeOn(BookLikeVO vo);

	public int bookLikeOff(BookLikeVO vo);

	public int bookLikeCount(int book_no);

	public int bookLikeCheck(BookLikeVO vo);
	
	public void bookLikeIncrement(int book_no);
	
	public void bookLikeDecrement(int book_no);
	
	public int bookCartCheck(BookCartVO vo);

	public void bookCartUpdate(BookCartVO vo);

	public void bookCartInsert(BookCartVO vo);
	
	public List<BookCartVO> bookCartListData(int member_id);
	
	public List<BookOrderVO> bookOrderListData(int member_id);

    public void bookOrderInsert(BookOrderVO vo);

    public void bookOrderDetailInsert(BookOrderDetailVO vo);
    
    public void bookOrderComplete(BookOrderVO orderVO, List<BookOrderDetailVO> detailList);
    
    public List<BookVO> bookBestData();
}
