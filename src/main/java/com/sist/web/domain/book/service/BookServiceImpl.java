package com.sist.web.domain.book.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sist.web.domain.book.mapper.BookMapper;
import com.sist.web.domain.book.vo.BookCartVO;
import com.sist.web.domain.book.vo.BookCommentVO;
import com.sist.web.domain.book.vo.BookLikeVO;
import com.sist.web.domain.book.vo.BookOrderDetailVO;
import com.sist.web.domain.book.vo.BookOrderVO;
import com.sist.web.domain.book.vo.BookVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
	private final BookMapper bMapper;

	@Override
	public List<BookVO> bookListData(Map map) {
		// TODO Auto-generated method stub
		return bMapper.bookListData(map);
	}

	@Override
	public int bookTotalCount(String category) {
		// TODO Auto-generated method stub
		return bMapper.bookTotalCount(category);
	}

	@Override
	public BookVO bookDetailData(int no) {
		// TODO Auto-generated method stub
		bMapper.bookHitIncrement(no);
		return bMapper.bookDetailData(no);
	}

	@Override
	public int bookFindCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return bMapper.bookFindCount(map);
	}

	@Override
	public List<BookVO> bookFindData(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return bMapper.bookFindData(map);
	}

	@Override
	public int bookLikeOn(BookLikeVO vo) {
		// TODO Auto-generated method stub
		int result = bMapper.bookLikeOn(vo);

		bMapper.bookLikeIncrement(vo.getBook_no());

		return result;
	}

	@Override
	public int bookLikeOff(BookLikeVO vo) {
		// TODO Auto-generated method stub
		int result = bMapper.bookLikeOff(vo);

		bMapper.bookLikeDecrement(vo.getBook_no());

		return result;
	}

	@Override
	public int bookLikeCount(int book_no) {
		// TODO Auto-generated method stub
		return bMapper.bookLikeCount(book_no);
	}

	@Override
	public int bookLikeCheck(BookLikeVO vo) {
		// TODO Auto-generated method stub
		return bMapper.bookLikeCheck(vo);
	}

	@Override
	public void bookLikeIncrement(int book_no) {
		// TODO Auto-generated method stub
		bMapper.bookLikeIncrement(book_no);
	}

	@Override
	public void bookLikeDecrement(int book_no) {
		// TODO Auto-generated method stub
		bMapper.bookLikeDecrement(book_no);
	}

	@Override
	public int bookCartCheck(BookCartVO vo) {
		// TODO Auto-generated method stub
		return bMapper.bookCartCheck(vo);
	}

	@Override
	public void bookCartUpdate(BookCartVO vo) {
		// TODO Auto-generated method stub
		bMapper.bookCartUpdate(vo);
	}

	@Override
	public void bookCartInsert(BookCartVO vo) {
		// TODO Auto-generated method stub
		bMapper.bookCartInsert(vo);
	}

	@Override
	public List<BookCartVO> bookCartListData(int member_id) {
		// TODO Auto-generated method stub
		return bMapper.bookCartListData(member_id);
	}

	@Override
	public List<BookOrderVO> bookOrderListData(int member_id) {
		// TODO Auto-generated method stub
		return bMapper.bookOrderListData(member_id);
	}

	@Override
	public void bookOrderInsert(BookOrderVO vo) {
		// TODO Auto-generated method stub
		bMapper.bookOrderInsert(vo);
	}

	@Override
	public void bookOrderDetailInsert(BookOrderDetailVO vo) {
		// TODO Auto-generated method stub
		bMapper.bookOrderDetailInsert(vo);
	}
	
	@Override
	public void bookOrderComplete(BookOrderVO vo, List<BookOrderDetailVO> detailList) {
		bMapper.bookOrderInsert(vo);
		for(BookOrderDetailVO detail : detailList) {
			detail.setBook_order_no(vo.getNo()); 
			bMapper.bookOrderDetailInsert(detail);
		}
	}

	@Override
	public List<BookVO> bookBestData() {
		// TODO Auto-generated method stub
		return bMapper.bookBestData();
	}

	@Override
	public List<BookCommentVO> bookCommentListData(int book_no) {
		// TODO Auto-generated method stub
		return bMapper.bookCommentListData(book_no);
	}

	@Override
	public int bookCommentCount(int book_no) {
		// TODO Auto-generated method stub
		return bMapper.bookCommentCount(book_no);
	}

	@Override
	public void bookCommentInsert(BookCommentVO vo) {
		// TODO Auto-generated method stub
		bMapper.bookCommentInsert(vo);
	}

	@Override
	public BookCommentVO bookCommentParentInfoData(int no) {
		// TODO Auto-generated method stub
		return bMapper.bookCommentParentInfoData(no);
	}

	@Override
	public void bookCommentStepIncrement(BookCommentVO vo) {
		// TODO Auto-generated method stub
		bMapper.bookCommentStepIncrement(vo);
	}

	@Override
	public void bookCommentReplyInsert(BookCommentVO vo) {
		// TODO Auto-generated method stub
		bMapper.bookCommentReplyInsert(vo);
	}

	@Override
	public void bookCommentDepthIncrement(int no) {
		// TODO Auto-generated method stub
		bMapper.bookCommentDepthIncrement(no);
	}

	@Override
	public void bookCommentUpdate(BookCommentVO vo) {
		// TODO Auto-generated method stub
		bMapper.bookCommentUpdate(vo);
	}

	@Override
	public BookCommentVO bookCommentInfoData(int no) {
		// TODO Auto-generated method stub
		return bMapper.bookCommentInfoData(no);
	}

	@Override
	public void bookCommentMsgUpdate(int no) {
		// TODO Auto-generated method stub
		bMapper.bookCommentMsgUpdate(no);
	}

	@Override
	public void bookCommentDelete(int no) {
		// TODO Auto-generated method stub
		bMapper.bookCommentDelete(no);
	}

	@Override
	public void bookCommentDepthDecrement(int no) {
		// TODO Auto-generated method stub
		bMapper.bookCommentDepthDecrement(no);
	}

	
}
