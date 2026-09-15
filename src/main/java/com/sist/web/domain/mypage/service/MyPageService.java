package com.sist.web.domain.mypage.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sist.web.domain.book.vo.BookCartVO;
import com.sist.web.domain.book.vo.BookOrderVO;
import com.sist.web.domain.enrollment.vo.*;
import com.sist.web.domain.member.vo.MemberVO;
import com.sist.web.domain.mypage.vo.CourseCartVO;
import com.sist.web.domain.mypage.vo.CoursePaymentVO;
import com.sist.web.domain.mypage.vo.MyMemberVO;

public interface MyPageService {
	public List<CourseEnrollmentVO> mypageCourseListData(int member_id);
	public List<CourseEnrollmentVO> lastAccessedCourse(int member_id);
	
	public int enrolledCount(int member_id);
	
	public MyMemberVO memberProfileData(int member_id);
	public void memberUpdateData(MemberVO vo);
	
	public List<CoursePaymentVO> coursePaymentListData(int page,int member_id,String order_status);
	public int coursePaymentTotalCount(int member_id);
	
	public int[] pages(String type, int page,int member_id,String order_status);
	
	public List<CourseCartVO> courseCartListData(int page,int member_id);
	public void coursePaymentAwaitRefund(int no,int member_id);
	
	public List<BookOrderVO> bookOrderListData(int page,int member_id,String order_status);
	public void bookOrderAwaitRefund(int no,int member_id);
	public int bookOrderTotalCount(int member_id);
	
	public void courseCartCheckout(int member_id,List<Integer> cList);

	
	public void courseEnrollmentInsert(int member_id, int course_no, int price);
	public int courseEnrollmentAlready(int member_id, int course_no);
	public void courseCartInsert(int member_id, int course_no);
	public int courseCartAlready(int member_id, int course_no);
	public void courseCartDelete(int member_id,int course_no);
	
	public List<BookCartVO> bookCartListData(int member_id,int page);
	
	public void bookCartDelete(int member_id,int book_no);
}
