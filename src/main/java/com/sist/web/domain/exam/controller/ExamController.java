package com.sist.web.domain.exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class ExamController {

	@GetMapping("/exam/list")
	public String examList(Model model){
		model. addAttribute("main_html","exam/list");
		return "main/main";
	}

	@GetMapping("/exam/detail")
	public String exam_detail(@RequestParam(value="theme",required=false)Integer theme,@RequestParam(value="count",defaultValue="20")int count, @RequestParam(value="examNo",required=false)Integer examNo, HttpSession session, Model model) {
		Integer mid=(Integer)session.getAttribute("member_id");
		String name=(String)session.getAttribute("name");
		model.addAttribute("mid", mid);
		model.addAttribute("name", name);
		model.addAttribute("theme", theme);
        model.addAttribute("count", count);
		model.addAttribute("examNo", examNo);
		model.addAttribute("main_html", "exam/detail");
		return "main/main";
	}

	@GetMapping("/exam/result")
	public String exam_result(@RequestParam("no")Integer enrollmentNo,Model model){
		model.addAttribute("enrollmentNo",enrollmentNo);
		model.addAttribute("main_html","exam/result");
		return "main/main";
	}

	@GetMapping("/exam/result_list")
	public String examResultList(Model model) {
		model.addAttribute("main_html", "exam/result_list");
		return "main/main";
	}
}
