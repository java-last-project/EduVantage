package com.sist.web.domain.book.mapper;
import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.sist.web.domain.book.vo.*;
@Mapper
public interface BookMapper {
	/*
	 * <select id="bookListData" resultType="com.sist.web.domain.book.vo.BookVO" parameterType="hashmap">
	        SELECT no, title, author, poster, like_count, price, pubdate, hit
	        FROM book
	        <if test="category != '전체'">
	            WHERE category = #{category}
	        </if>
	        <choose>
	            <when test="sort == '출간일 순'">ORDER BY pubdate DESC</when>
	            <when test="sort == '낮은 가격순'">ORDER BY price ASC</when>
	            <when test="sort == '높은 가격순'">ORDER BY price DESC</when>
	            <when test="sort == '조회 수 순'">ORDER BY hit DESC</when>
	            <otherwise>ORDER BY book_id ASC</otherwise>
	        </choose>
	        OFFSET #{start} ROWS FETCH NEXT 12 ROWS ONLY
	    </select>
	*/
	public List<BookVO> bookListData(Map map);
	/*
	    <select id="bookTotalPage" resultType="int" parameterType="String">
	        SELECT CEIL(COUNT(*)/12.0) 
	        FROM book
	        <if test="category != '전체'">
	            WHERE category = #{category}
	        </if>
	    </select>
	 */
	public int bookTotalPage(String category);
}
