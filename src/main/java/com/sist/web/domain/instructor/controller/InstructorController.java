package com.sist.web.domain.instructor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.instructor.service.InstructorService;
import com.sist.web.domain.member.vo.MemberVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class InstructorController {
	private final InstructorService iService;
	
	@GetMapping("/instructor/course")
	public String instructor_course(HttpSession session, Model model)
	{
		int member_id = (int)session.getAttribute("member_id");
		List<Map<String, Object>> list = iService.InstCourseDataList(member_id);
		
		model.addAttribute("list", list);
		
		model.addAttribute("instructor_html", "instructor/course");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
	
	@GetMapping("/instructor/course_detail")
	public String instructor_course_detail(HttpSession session, @RequestParam("course_no") int course_no, Model model)
	{
		//int member_id = (int)session.getAttribute("member_id");
		 
		List<Map<String,Object>> sList = iService.InstCourseEnrollStudList(course_no);
		CourseVO vo = iService.InstCourseDetailData(course_no);
		
		model.addAttribute("vo", vo);
		model.addAttribute("sList", sList);
		
		model.addAttribute("instructor_html", "instructor/course_detail");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
	
	@GetMapping("/instructor/profile")
	public String instructor_profile(HttpSession session, Model model)
	{
		int member_id=(int)session.getAttribute("member_id");
		MemberVO vo = iService.InstProfileData(member_id);
		
		model.addAttribute("vo", vo);
		
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
