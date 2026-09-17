package com.sist.web.domain.course.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sist.web.domain.course.service.CourseService;
import com.sist.web.domain.course.vo.CourseVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CourseController {
	private final CourseService cService;
	
	@Value("${portone.imp-code}")
	private String impCode;
	
	@GetMapping("/course/list")
	public String course_list(Model model) {
		model.addAttribute("main_html", "course/list");
		return "main/main";
	}
	
	@GetMapping("/course/detail")
	public String course_detail(Model model,@RequestParam("no")int no) {
		CourseVO vo=cService.courseDetail(no);
		if(vo==null) {
			return "redirect:/course/list";
		}
		//System.out.println(vo);
		model.addAttribute("impCode", impCode);
		model.addAttribute("course", vo);
		model.addAttribute("main_html", "course/detail");
		return "main/main";
	}
}
