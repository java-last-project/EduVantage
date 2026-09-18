package com.sist.web.domain.mypage.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.domain.book.vo.BookCartVO;
import com.sist.web.domain.book.vo.BookOrderVO;
import com.sist.web.domain.member.vo.MemberVO;
import com.sist.web.domain.mypage.service.*;
import com.sist.web.domain.mypage.vo.CourseCartVO;
import com.sist.web.domain.mypage.vo.CoursePaymentVO;
import com.sist.web.domain.mypage.vo.MyMemberVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class MyPageRestController {
	private final MyPageService mService;
	private final PasswordEncoder passwordEncoder;
	
	private Map commonsBookOrdersListData(int page,int member_id,String order_status) {
		Map map = new HashMap();
		List<BookOrderVO> bList=mService.bookOrderListData(page, member_id,order_status);
		int[] pages=mService.pages("book_order",page, member_id,order_status);
		map.put("bList", bList);
		map.put("page", pages[0]);
		map.put("totalpage", pages[1]);
		map.put("startpage", pages[2]);
		map.put("endpage", pages[3]);
		map.put("bCount", pages[4]);
		return map;
	}
	
	@GetMapping("/mypage/profile_update")
	public ResponseEntity<Map> mypage_profile_update(
			HttpSession session){
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			int eCount=mService.enrolledCount(member_id);
			int bCount=mService.myBoardCount(member_id);
			int vCount=mService.myEvaluationCount(member_id);
			MyMemberVO vo=mService.memberProfileData(member_id);
			map.put("eCount", eCount);
			map.put("bCount", bCount);
			map.put("vCount", vCount);
			map.put("vo", vo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	// 프로필 수정
	@PostMapping("/mypage/profile_process")
	public ResponseEntity<Void> mypage_profile_process(
			MemberVO vo,
			HttpSession session) {
	    // 1. 비밀번호 평문 가져오기
	    String rawPassword = vo.getPassword();
	    // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(rawPassword);
	    // 3. 암호화된 비밀번호를 VO에 다시 세팅 
	    vo.setPassword(encodedPassword);
	    // 4. DB에 저장 
	    //System.out.println("vo: "+vo);
	    mService.memberUpdateData(vo);
	    
	    // session update
	    session.setAttribute("name", vo.getName());
	    return ResponseEntity.status(HttpStatus.FOUND)
	    		             .location(URI.create("/mypage"))
	    		             .build();
	}
	
	// 강의 구매 목록 api
	@GetMapping("/mypage/course_orders_vue")
	public ResponseEntity<Map> mypage_course_orders_vue(
			@RequestParam("page") int page,
			@RequestParam("order_status") String order_status,
			HttpSession session
			){
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			List<CoursePaymentVO> cList=mService.coursePaymentListData(page,member_id,order_status);
			int[] pages=mService.pages("course_payment",page, member_id,order_status);
			map.put("cList", cList);
			map.put("page", pages[0]);
			map.put("totalpage", pages[1]);
			map.put("startpage", pages[2]);
			map.put("endpage", pages[3]);
			map.put("cCount", pages[4]);
			map.put("cTotalCount", mService.coursePaymentTotalCount(member_id));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	// 강의 구매 목록 api
	@GetMapping("/mypage/course_carts_vue")
	public ResponseEntity<Map> mypage_course_carts_vue(
			@RequestParam("page") int page,
			HttpSession session){
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			List<CourseCartVO> cList=mService.courseCartListData(page,member_id);
			int[] pages=mService.pages("course_cart",page, member_id,"");
			map.put("cList", cList);
			map.put("page", pages[0]);
			map.put("totalpage", pages[1]);
			map.put("startpage", pages[2]);
			map.put("endpage", pages[3]);
			map.put("cCount", pages[4]);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	// 도서 구매 목록 api
	@GetMapping("/mypage/book_orders_vue")
	public ResponseEntity<Map> mypage_book_orders_vue(
			@RequestParam("page") int page,
			@RequestParam("order_status") String order_status,
			HttpSession session
			){
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			map=commonsBookOrdersListData(page, member_id,order_status);
			map.put("bTotalCount", mService.bookOrderTotalCount(member_id));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	// course 환불 대기 상태 변경
	@PutMapping("/mypage/course_wait_refund_vue")
	public ResponseEntity<Map> mypage_course_wait_refund_vue(
			@RequestParam("page") int page,
			@RequestParam("no") int no,
			@RequestParam("order_status") String order_status,
			HttpSession session
			){
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			mService.coursePaymentAwaitRefund(no, member_id);
			List<CoursePaymentVO> cList=mService.coursePaymentListData(page,member_id,order_status);
			int[] pages=mService.pages("course_payment",page, member_id,order_status);
			map.put("cList", cList);
			map.put("page", pages[0]);
			map.put("totalpage", pages[1]);
			map.put("startpage", pages[2]);
			map.put("endpage", pages[3]);
			map.put("cCount", pages[4]);
			map.put("cTotalCount", mService.coursePaymentTotalCount(member_id));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	// book 환불 대기 상태 변경
	@PutMapping("/mypage/book_wait_refund_vue")
	public ResponseEntity<Map> mypage_book_wait_refund_vue(
			@RequestParam("page") int page,
			@RequestParam("no") int no,
			@RequestParam("order_status") String order_status,
			HttpSession session
			){
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			mService.bookOrderAwaitRefund(no, member_id);
			map=commonsBookOrdersListData(page, member_id,order_status);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	
	@PostMapping("/course/enrollment_insert")
	public ResponseEntity<Map> course_enrollment_insert_vue(
			@RequestParam("course_no") int course_no,
			@RequestParam("price") int price,
			HttpSession session
			) {
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			mService.courseEnrollmentInsert(member_id, course_no, price);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/course/enrollment_already")
	public ResponseEntity<Map> course_enrollment_already(
			@RequestParam("course_no") int course_no,
			HttpSession session
			) {
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			int already=mService.courseEnrollmentAlready(member_id, course_no);
			if(already==0) {
				map.put("enrolled", false);
			}
			else map.put("enrolled", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	@PostMapping("/course/cart_insert")
	public ResponseEntity<Map> course_cart_insert_vue(
			@RequestParam("course_no") int course_no,
			HttpSession session
			) {
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			mService.courseCartInsert(member_id, course_no);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/course/cart_already")
	public ResponseEntity<Map> course_cart_already(
			@RequestParam("course_no") int course_no,
			HttpSession session
			) {
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			int already=mService.courseCartAlready(member_id, course_no);
			if(already==0) {
				map.put("cart", false);
			}
			else map.put("cart", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	@PostMapping("/mypage/course_cart/checkout")
	public ResponseEntity<Void> mypage_course_cart_checkout(
			@RequestBody Map<String, List<Integer>> body,
			HttpSession session
			) {
		try {
			int member_id=(int)session.getAttribute("member_id");
			mService.courseCartCheckout(member_id, body.get("course_list"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/mypage/course_cart/delete")
	public ResponseEntity<Void> mypage_course_cart_checkout(
			@RequestParam("course_list") List<Integer> course_list,
			HttpSession session
			) {
		try {
			int member_id=(int)session.getAttribute("member_id");
			//mService.courseCartCheckout(member_id, body.get("course_list"));
			for(int cno: course_list) {
				mService.courseCartDelete(member_id, cno);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/mypage/book_carts_vue")
	public ResponseEntity<Map> mypage_book_carts_vue(
			@RequestParam("page") int page,
			HttpSession session){
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			List<BookCartVO> bList=mService.bookCartListData(member_id,page);
			int[] pages=mService.pages("book_cart",page, member_id,"");
			map.put("bList", bList);
			map.put("page", pages[0]);
			map.put("totalpage", pages[1]);
			map.put("startpage", pages[2]);
			map.put("endpage", pages[3]);
			map.put("bCount", pages[4]);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	@DeleteMapping("/mypage/book_cart/delete")
	public ResponseEntity<Void> mypage_book_cart_delete(
			//@RequestParam("book_no") int book_no,
			@RequestParam("book_list") List<Integer> book_list,
			HttpSession session
			) {
		try {
			int member_id=(int)session.getAttribute("member_id");
			for(int bno: book_list) {
				mService.bookCartDelete(member_id, bno);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok().build();
	}
}
