package com.sist.web.domain.book.mapper;
import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.sist.web.domain.book.vo.*;
@Mapper
public interface BookMapper {

	public List<BookVO> bookListData(Map map);

	public int bookTotalPage(String category);
	
	public int bookTotalCount(String category);
}
