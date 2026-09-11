package com.sist.web.domain.book.mapper;
import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.sist.web.domain.book.vo.*;
@Mapper
public interface BookMapper {

	public List<BookVO> bookListData(Map map);
	
	public int bookTotalCount(String category);
	
	public BookVO bookDetailData (int no);
	
	public void bookHitIncrement (int no);
	
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
    
    public List<BookVO> bookBestData();
    
    public List<BookCommentVO> bookCommentListData(int book_no);
    
    public int bookCommentCount(int book_no);
   
    public void bookCommentInsert(BookCommentVO vo);
    
    /*
     * <!-- 댓글 목록 출력 -->
	<select id="bookCommentListData" resultType="com.sist.web.domain.book.vo.BookCommentVO" parameterType="int">
	    SELECT no, book_no, member_id, name, msg,
	           TO_CHAR(regdate, 'YYYY-MM-DD HH24:MI') as dbday,
	           group_tab, group_step, group_id, root, depth
	    FROM book_reply
	    WHERE book_no = #{book_no}
	    ORDER BY group_id DESC, group_step ASC
	</select>
	
	<!-- 도서별 총 댓글 개수 -->
	<select id="bookCommentCount" resultType="int" parameterType="int">
	    SELECT COUNT(*) 
	    FROM book_reply 
	    WHERE book_no = #{book_no}
	</select>
	
	<!-- 댓글 작성 -->
	<insert id="bookCommentInsert" parameterType="com.sist.web.domain.book.vo.BookCommentVO">
	    <selectKey keyProperty="group_id" resultType="int" order="BEFORE">
	        SELECT NVL(MAX(group_id)+1, 1) FROM book_reply
	    </selectKey>
	    INSERT INTO book_comment (
	        no, 
	        book_no, 
	        member_id, 
	        name, 
	        msg, 
	        regdate,
	        group_id, 
	        group_step, 
	        group_tab, 
	        root, 
	        depth
	    ) VALUES (
	        br_no_seq.nextval, 
	        #{book_no}, 
	        #{member_id}, 
	        #{name}, 
	        #{msg}, 
	        SYSDATE,
	        #{group_id}, 0, 0, 0, 0
	    )
	</insert>
	
	<!-- 대댓글 작성을 위한 부모 정보 조회 -->
	<select id="bookCommentParentInfoData" resultType="com.sist.web.domain.book.vo.BookCommentVO" parameterType="int">
	    SELECT group_id, group_step, group_tab FROM book_reply WHERE no = #{no}
	</select>
	*/
    public List<BookCommentVO> bookCommentParentInfoData(int book_no);
    /*
	<!-- 대댓글 순서 밀어내기 -->
	<update id="bookCommentStepIncrement" parameterType="com.sist.web.domain.book.vo.BookCommentVO">
	    UPDATE book_reply SET group_step = group_step + 1
	    WHERE group_id = #{group_id} AND group_step > #{group_step}
	</update>
	*/
    public void bookCommentStepIncrement(BookCommentVO vo);
    /*
	<!-- 대댓글 등록 -->
	<insert id="bookCommentReplyInsert" parameterType="com.sist.web.domain.book.vo.BookCommentVO">
	    INSERT INTO book_reply (
	        no, book_no, member_id, name, msg, regdate, group_id, group_step, group_tab, root, depth
	    ) VALUES (
	        br_no_seq.nextval, #{book_no}, #{member_id}, #{name}, #{msg}, SYSDATE,
	        #{group_id}, #{group_step} + 1, #{group_tab} + 1, #{root}, 0
	    )
	</insert>
	*/
    public void bookCommentReplyInsert(BookCommentVO vo);
    /*
	<!-- 부모 댓글 depth 증가 -->
	<update id="bookCommentDepthIncrement" parameterType="int">
	    UPDATE book_reply SET depth = depth + 1 WHERE no = #{no}
	</update>
	*/
    public void bookCommentDepthIncrement(int no);
    /*
	<!-- 댓글 수정 -->
	<update id="bookCommentUpdate" parameterType="com.sist.web.domain.book.vo.BookCommentVO">
	    UPDATE book_reply SET msg = #{msg} WHERE no = #{no}
	</update>
	*/
    public void bookCommentUpdate(BookCommentVO vo);
    /*
	<!-- 댓글 삭제를 위한 정보 조회 -->
	<select id="bookCommentInfoData" resultType="com.sist.web.domain.book.vo.BookCommentVO" parameterType="int">
	    SELECT depth, root FROM book_reply WHERE no = #{no}
	</select>
	*/
    public List<BookCommentVO> bookCommentInfoData(int no);
    /*
	<!-- 자식이 있는 댓글 내용 변경 -->
	<update id="bookCommentMsgUpdate" parameterType="int">
	    UPDATE book_reply SET msg = '관리자가 삭제한 댓글입니다.' WHERE no = #{no}
	</update>
	*/
    public void bookCommentMsgUpdate(int no);
    /*
	<!-- 실제 댓글 삭제 -->
	<delete id="bookCommentDelete" parameterType="int">
	    DELETE FROM book_reply WHERE no = #{no}
	</delete>
	*/
    public void bookCommentDelete(int no);
    /*
	<!-- 부모 댓글 depth 감소 -->
	<update id="bookCommentDepthDecrement" parameterType="int">
	    UPDATE book_reply SET depth = depth - 1 WHERE no = #{no}
	</update>
     */
    public void bookCommentDepthDecrement(int no);
}
