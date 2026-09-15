package com.sist.web.domain.mypage.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MyPageCartMapper {
	// 강의 수강 신청
	@Insert("INSERT INTO course_payment(no,member_id,course_no,price,order_status) "
			+ "VALUES (cp_no_seq.nextval,#{member_id},#{course_no},#{price},'결제완료')")
	public void coursePaymentInsert(
			@Param("member_id") int member_id,
			@Param("course_no") int course_no,
			@Param("price") int price);
	
	@Insert("INSERT INTO course_enrollment(no,member_id,course_no) "
			+ "VALUES (cen_no_seq.nextval,#{member_id},#{course_no})")
	public void courseEnrollmentInsert(
			@Param("member_id") int member_id,
			@Param("course_no") int course_no);
	
	// 이미 수강중인지 확인
	@Select("SELECT COUNT(*) FROM course_enrollment WHERE member_id=#{member_id} "
			+ "AND course_no=#{course_no}")
	public int courseEnrollmentAlready(
			@Param("member_id") int member_id,
			@Param("course_no") int course_no);
	
	// 강의 장바구니 담기
	@Insert("INSERT INTO course_cart(no,member_id,course_no) "
			+ "VALUES (cc_no_seq.nextval,#{member_id},#{course_no})")
	public void courseCartInsert(
			@Param("member_id") int member_id,
			@Param("course_no") int course_no);
	
	// 이미 장바구니에 있는지 확인
	@Select("SELECT COUNT(*) FROM course_cart WHERE member_id=#{member_id} "
			+ "AND course_no=#{course_no}")
	public int courseCartAlready(
			@Param("member_id") int member_id,
			@Param("course_no") int course_no);	
	
	// 장바구니 삭제
	@Delete("DELETE FROM course_cart WHERE member_id=#{member_id} AND course_no=#{course_no}")
	public void courseCartDelete(
			@Param("member_id") int member_id,
			@Param("course_no") int course_no);
}
