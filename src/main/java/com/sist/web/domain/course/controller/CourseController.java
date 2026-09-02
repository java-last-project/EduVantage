package com.sist.web.domain.course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CourseController {
	
	@GetMapping("/course/list")
	public String course_list(Model model) {
		model.addAttribute("main_html", "course/list");
		return "main/main";
	}
	
	@GetMapping("/course/detail")
	public String course_detail(Model model,@RequestParam("no")int no) {
		
		model.addAttribute("main_html", "course/detail");
		return "main/main";
	}
}
