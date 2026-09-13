package com.sist.web.domain.mypage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.domain.book.vo.BookOrderDetailVO;
import com.sist.web.domain.book.vo.BookOrderVO;
import com.sist.web.domain.enrollment.vo.*;
import com.sist.web.domain.member.vo.MemberVO;
import com.sist.web.domain.mypage.vo.CourseCartVO;
import com.sist.web.domain.mypage.vo.CoursePaymentVO;
import com.sist.web.domain.mypage.vo.MyMemberVO;
import com.sist.web.domain.mypage.mapper.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
	private final MyPageMapper mMapper;
	private final MyPageOrderMapper oMapper;
	
	@Override
	public List<CourseEnrollmentVO> mypageCourseListData(int member_id) {
		// TODO Auto-generated method stub
		return mMapper.mypageCourseListData(member_id);
	}
	@Override
	public int enrolledCount(int member_id) {
		// TODO Auto-generated method stub
		return mMapper.enrolledCount(member_id);
	}
	@Override
	public List<CourseEnrollmentVO> lastAccessedCourse(int member_id) {
		// TODO Auto-generated method stub
		return mMapper.lastAccessedCourse(member_id);
	}
	@Override
	public MyMemberVO memberProfileData(int member_id) {
		// TODO Auto-generated method stub
		return mMapper.memberProfileData(member_id);
	}
	@Override
	public void memberUpdateData(MemberVO vo) {
		// TODO Auto-generated method stub
		mMapper.memberUpdateData(vo);
	}
	@Override
	public List<CoursePaymentVO> coursePaymentListData(int page,int member_id,String order_status) {
		// TODO Auto-generated method stub
		final int ROWSIZE=3;
		int start=(page*ROWSIZE)-ROWSIZE;
		return oMapper.coursePaymentListData(start,member_id,order_status);
	}
	@Override
	public int[] pages(String type, int page,int member_id,String order_status) {
		// TODO Auto-generated method stub
		int count=0;
		if(type.equals("course_payment")) count=oMapper.coursePaymentRowCount(member_id,order_status);
		else if(type.equals("course_cart")) count=oMapper.courseCartRowCount(member_id);
		else if(type.equals("book_order")) count=oMapper.bookOrderRowCount(member_id,order_status);
		else if(type.equals("book_cart")) count=oMapper.courseCartRowCount(member_id);
		
		int totalpage=(int)Math.ceil(count/3.0);
		final int BLOCK=10;
		int startpage=((page-1)/BLOCK*BLOCK)+1;
		int endpage=((page-1)/BLOCK*BLOCK)+BLOCK;
		if(endpage>totalpage) endpage=totalpage;
		
		int[] pages= {page,totalpage,startpage,endpage,count};
		return pages;
	}
	@Override
	public List<CourseCartVO> courseCartListData(int page, int member_id) {
		// TODO Auto-generated method stub
		final int ROWSIZE=3;
		int start=(page*ROWSIZE)-ROWSIZE;
		return oMapper.courseCartListData(start, member_id);
	}
	@Override
	public List<BookOrderVO> bookOrderListData(int page,int member_id,String order_status) {
		// TODO Auto-generated method stub
		final int ROWSIZE=3;
		int start=(page*ROWSIZE)-ROWSIZE;
		List<BookOrderVO> list=oMapper.bookOrderListData(start,member_id,order_status);
		for(BookOrderVO vo:list) {
			vo.setDetailList(oMapper.bookOrderDetailListData(vo.getNo()));
		}
		return list;
	}
	@Override
	public void bookOrderAwaitRefund(int no,int member_id) {
		// TODO Auto-generated method stub
		oMapper.bookOrderAwaitRefund(no,member_id);
	}
	@Override
	public int bookOrderTotalCount(int member_id) {
		// TODO Auto-generated method stub
		return oMapper.bookOrderTotalCount(member_id);
	}
	@Override
	public int coursePaymentTotalCount(int member_id) {
		// TODO Auto-generated method stub
		return oMapper.coursePaymentTotalCount(member_id);
	}

}
