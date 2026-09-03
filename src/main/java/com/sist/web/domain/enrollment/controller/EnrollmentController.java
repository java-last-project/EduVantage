package com.sist.web.domain.enrollment.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.service.EnrollmentService;
import com.sist.web.domain.enrollment.vo.CourseEvaluationVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/enrollment/{course_no}")
public class EnrollmentController {
	private final EnrollmentService eService;
	
	@ModelAttribute
    public void setCourseNo(@PathVariable("course_no") int course_no, Model model) {
        model.addAttribute("course_no", course_no);
        model.addAttribute("title", eService.courseTitleData(course_no));
    }
	
	@GetMapping
	public String enrollment_dashboarad(@PathVariable("course_no") int course_no, Model model) {
		CourseVO vo=eService.courseDetailData(course_no);
		model.addAttribute("vo",vo);
		model.addAttribute("menu","dashboard");
		model.addAttribute("enrollment_html","enrollment/dashboard");
		return "enrollment/layout/main";
	}
	
	@GetMapping("/video")
	public String enrollment_video(Model model) {
		model.addAttribute("menu","video");
		model.addAttribute("enrollment_html","enrollment/video");
		return "enrollment/layout/main";
	}
	
	@GetMapping("/notice")
	public String enrollment_notice(Model model) {
		model.addAttribute("menu","notice");
		model.addAttribute("enrollment_html","enrollment/notice");
		return "enrollment/layout/main";
	}
	
	@GetMapping("/qna")
	public String enrollment_qna(Model model) {
		model.addAttribute("menu","qna");
		model.addAttribute("enrollment_html","enrollment/qna");
		return "enrollment/layout/main";
	}
	
	@GetMapping("/exam")
	public String enrollment_exam(Model model) {
		model.addAttribute("menu","exam");
		model.addAttribute("enrollment_html","enrollment/exam");
		return "enrollment/layout/main";
	}
	
	@GetMapping("/evaluation")
	public String enrollment_evaluation(@PathVariable("course_no") int course_no, Model model) {
		model.addAttribute("menu","evaluation");
		CourseVO vo=eService.courseDetailData(course_no);
		int eCount=eService.evaluationCount(course_no);
		List<CourseEvaluationVO> eList=eService.evaluationListData(course_no);

		model.addAttribute("vo",vo);
		model.addAttribute("eCount",eCount);
		model.addAttribute("eList",eList);
		model.addAttribute("enrollment_html","enrollment/evaluation");
		return "enrollment/layout/main";
	}
}
