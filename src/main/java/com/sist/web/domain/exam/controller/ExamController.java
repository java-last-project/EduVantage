package com.sist.web.domain.exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExamController {
	
	@GetMapping("/exam/detail")
	public String exam_detail(Model model) {
		model.addAttribute("main_html", "exam/detail");
		return "main/main";
	}
}
