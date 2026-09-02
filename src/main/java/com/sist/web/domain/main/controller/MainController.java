package com.sist.web.domain.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;
import com.sist.web.domain.course.service.CourseService;
import com.sist.web.domain.course.vo.CourseVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final CourseService cService;
	
	@GetMapping("/")
	public String main(Model model) {
		List<CourseVO> cList=cService.courseMainList();
		model.addAttribute("cList", cList);
		model.addAttribute("main_html", "main/home");
		return "main/main";
	}
}
