package com.sist.web.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
	
	@GetMapping("/admin/member")
	public String admin_member(Model model)
	{
		model.addAttribute("admin_html", "admin/member");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/course")
	public String admin_course(Model model)
	{
		model.addAttribute("admin_html", "admin/course");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/order")
	public String admin_order(Model model)
	{
		model.addAttribute("admin_html", "admin/order");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/notice")
	public String admin_notice(Model model)
	{
		model.addAttribute("admin_html", "admin/notice");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/qna")
	public String admin_qna(Model model)
	{
		model.addAttribute("admin_html", "admin/qna");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/dashboard")
	public String admin_dashboard(Model model)
	{
		model.addAttribute("admin_html", "admin/dashboard");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}

}
