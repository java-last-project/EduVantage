package com.sist.web.domain.mypage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class MyPageController {
	//private final CourseService service;
	
	@GetMapping("/mypage")
	public String mypage_profile(Model model) {
		model.addAttribute("active","profile");
		model.addAttribute("main_html","mypage/layout/main");
		model.addAttribute("mypage_html","mypage/profile");
		return "main/main";
	}
	
	@GetMapping("/mypage/dashboard")
	public String mypage_dashboard(Model model) {
		model.addAttribute("active","dashboard");
		model.addAttribute("main_html","mypage/layout/main");
		model.addAttribute("mypage_html","mypage/dashboard");
		return "main/main";
	}
	
	@GetMapping("/mypage/courses")
	public String mypage_course(Model model, HttpSession session) {
		String member_id=(String)session.getAttribute("id");
		//List<CourseEnrollmentVO> list=service.courseEnrollmentListData(member_id);
		//System.out.println(list);
		//model.addAttribute("cList",list);
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
