package com.sist.web.domain.mypage.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sist.web.domain.mypage.service.MyPageService;
import com.sist.web.domain.mypage.vo.CourseEnrollmentVO;
import com.sist.web.domain.mypage.vo.MyMemberVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageController {
	private final MyPageService mService;
	
	@GetMapping("/mypage")
	public String mypage_profile(Model model) {
		int member_id=3;
		int eCount=mService.enrolledCount(member_id);
		MyMemberVO vo=mService.memberProfileData(member_id);
		model.addAttribute("eCount",eCount);
		model.addAttribute("vo",vo);
		model.addAttribute("active","profile");
		model.addAttribute("main_html","mypage/layout/main");
		model.addAttribute("mypage_html","mypage/profile");
		return "main/main";
	}
	
	@GetMapping("/mypage/dashboard")
	public String mypage_dashboard(Model model) {
		int member_id=3;
		List<CourseEnrollmentVO> list=mService.lastAccessedCourse(member_id);
		model.addAttribute("cLastList",list);
		model.addAttribute("active","dashboard");
		model.addAttribute("main_html","mypage/layout/main");
		model.addAttribute("mypage_html","mypage/dashboard");
		return "main/main";
	}
	
	@GetMapping("/mypage/courses")
	public String mypage_course(Model model, HttpSession session) {
		//int member_id=(int)session.getAttribute("id");
		int member_id=3;
		List<CourseEnrollmentVO> list=mService.mypageCourseListData(member_id);
		//System.out.println(list);
		model.addAttribute("cList",list);
		model.addAttribute("active","courses");
		model.addAttribute("main_html","mypage/layout/main");
		model.addAttribute("mypage_html","mypage/courses");
		return "main/main";
	}
	
	@GetMapping("/mypage/carts")
	public String mypage_carts(Model model) {
		model.addAttribute("active","carts");
		model.addAttribute("main_html","mypage/layout/main");
		model.addAttribute("mypage_html","mypage/carts");
		return "main/main";
	}
	
	@GetMapping("/mypage/orders")
	public String mypage_orders(Model model) {
		model.addAttribute("active","orders");
		model.addAttribute("main_html","mypage/layout/main");
		model.addAttribute("mypage_html","mypage/orders");
		return "main/main";
	}
}
