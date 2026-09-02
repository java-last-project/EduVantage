package com.sist.web.domain.course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CourseController {
	
	@GetMapping("/course/list")
	public String course_list(Model model) {
		model.addAttribute("main_html", "course/list");
		return "main/main";
	}
}
