package com.sist.web.domain.instructor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InstructorController {
	@GetMapping("/instructor/course")
	public String instructor_course(Model model)
	{
		model.addAttribute("instructor_html", "instructor/course");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
	
	@GetMapping("/instructor/profile")
	public String instructor_profile(Model model)
	{
		model.addAttribute("instructor_html", "instructor/profile");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
	
	@GetMapping("/instructor/qna")
	public String instructor_qna(Model model)
	{
		model.addAttribute("instructor_html", "instructor/qna");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
}
