package com.sist.web.domain.book.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sist.web.domain.book.mapper.BookMapper;
import com.sist.web.domain.book.vo.BookVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService{
	private final BookMapper bMapper;
	@Override
	public List<BookVO> bookListData(Map map) {
		// TODO Auto-generated method stub
		return bMapper.bookListData(map);
	}

	 @Override
	    public int[] bookTotalPage(int page, String category) {
	        int totalpage = bMapper.bookTotalPage(category);
	        int BLOCK = 10; // 화면에 보여줄 페이지 번호 개수
	        int startPage = ((page - 1) / BLOCK) * BLOCK + 1;
	        int endPage = startPage + BLOCK - 1;
	        if (endPage > totalpage) {
	            endPage = totalpage;
	        }
	        // 배열 반환
	        return new int[]{page, totalpage, startPage, endPage};
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

	






}
