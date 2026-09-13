package com.sist.web.domain.mypage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.mypage.vo.CourseCartVO;
import com.sist.web.domain.mypage.vo.CoursePaymentVO;
import com.sist.web.domain.book.vo.*;

@Mapper
@Repository
public interface MyPageOrderMapper {
	public List<CoursePaymentVO> coursePaymentListData(
			@Param("start") int start,
			@Param("member_id") int member_id,
			@Param("order_status") String order_status);
	
	public int coursePaymentRowCount(
			@Param("member_id") int member_id,
			@Param("order_status") String order_status
			);
	
	@Select("SELECT COUNT(*) FROM course_payment WHERE member_id=#{member_id}")
	public int coursePaymentTotalCount(int member_id);
	
	public List<CourseCartVO> courseCartListData(
			@Param("start") int start,
			@Param("member_id") int member_id);
	@Select("SELECT COUNT(*) FROM course_cart "
			+ "WHERE member_id=#{member_id}")
	public int courseCartRowCount(int member_id);
	

	
	
	// 바로구매 주문 데이터 조회
	public List<BookOrderVO> bookOrderListData(
			@Param("start") int start,
			@Param("member_id") int member_id,
			@Param("order_status") String order_status
			);
	public List<BookOrderDetailVO> bookOrderDetailListData(int book_order_no);
	
	public int bookOrderRowCount(
			@Param("member_id") int member_id,
			@Param("order_status") String order_status
			);
	
	@Select("SELECT COUNT(*) FROM book_order WHERE member_id=#{member_id}")
	public int bookOrderTotalCount(int member_id);
	
	// 환불 대기
	@Update("UPDATE book_order SET order_status='환불대기' "
			+ "WHERE no=#{no} AND member_id=#{member_id} AND order_status='결제완료'")
	public void bookOrderAwaitRefund(
			@Param("no") int no,
			@Param("member_id") int member_id
			);
}
