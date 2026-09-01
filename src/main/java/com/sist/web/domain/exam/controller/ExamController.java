package com.sist.web.domain.exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class ExamController {
	
	@GetMapping("/exam/detail")
	public String exam_detail(@RequestParam(value="theme",required=false)Integer theme,@RequestParam(value="count",defaultValue="20")int count, HttpSession session, Model model) {
		String mid=(String)session.getAttribute("member_id");
		String name=(String)session.getAttribute("name");
		model.addAttribute("mid", mid);
		model.addAttribute("name", name);
		model.addAttribute("theme", theme);
        model.addAttribute("count", count);
		model.addAttribute("main_html", "exam/detail");
		return "main/main";
	}
}
