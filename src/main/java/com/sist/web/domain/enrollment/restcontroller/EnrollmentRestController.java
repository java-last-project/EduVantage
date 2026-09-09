package com.sist.web.domain.enrollment.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.service.EnrollmentService;
import com.sist.web.domain.enrollment.vo.CourseEvaluationLikeVO;
import com.sist.web.domain.enrollment.vo.CourseEvaluationVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EnrollmentRestController {
	private final EnrollmentService eService;
	
	public Map commonsListData(int page, int course_no, int member_id) {
		Map map=new HashMap();
		List<CourseEvaluationVO> eList=eService.evaluationListData(page,course_no,member_id);
		int[] pages=eService.pages(page, course_no);
		double star=eService.courseStarData(course_no);
		map.put("eList", eList);
		
		map.put("page", pages[0]);
		map.put("totalpage", pages[1]);
		map.put("startpage", pages[2]);
		map.put("endpage", pages[3]);
		map.put("eCount", pages[4]);
		map.put("star", star);
		return map;
	}
	
	@GetMapping("/enrollment/course_vue")
	public ResponseEntity course_vue(
			@RequestParam("course_no") int course_no
			) {
		Map map=new HashMap();
		try {
			CourseVO vo=eService.courseData(course_no);
			map.put("vo", vo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/enrollment/myevaluation_vue")
	public ResponseEntity myevaluation_vue(
			@RequestParam("course_no") int course_no,
			HttpSession session
			) {
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			CourseEvaluationVO evo=eService.evaluationMyData(course_no, member_id);
			map.put("evo", evo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}	

	@GetMapping("/enrollment/evaluation_vue")
	public ResponseEntity evaluation_vue(
			@RequestParam("page") int page,
			@RequestParam("course_no") int course_no,
			HttpSession session
			) {
		Map map=new HashMap();
		try {
			map=commonsListData(page, course_no, (int)session.getAttribute("member_id"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	@PostMapping("/enrollment/evaluation_insert_vue")
	public ResponseEntity evaluation_insert_vue(
			@RequestBody CourseEvaluationVO vo,
			HttpSession session
			) {
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			vo.setMember_id(member_id);
			eService.evaluationInsert(vo);
			map=commonsListData(1, vo.getCourse_no(),member_id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	@DeleteMapping("/enrollment/evaluation_delete_vue")
	public ResponseEntity evaluation_insert_vue(
			@RequestParam("ce_no") int ce_no,
			@RequestParam("course_no") int course_no,
			@RequestParam("curpage") int curpage,
			HttpSession session
			) {
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			eService.evaluationDelete(ce_no, course_no);
			map=commonsListData(curpage, course_no,member_id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	@PutMapping("/enrollment/evaluation_update_vue")
	public ResponseEntity evaluation_update_vue(
			@RequestBody CourseEvaluationVO vo,
			HttpSession session
			) {
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			eService.evaluationUpdate(vo);
			map=commonsListData(vo.getCurpage(), vo.getCourse_no(),member_id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	// 강의평가 좋아요 클릭
	@PostMapping("/enrollment/evaluation_likeon_vue")
	public ResponseEntity evaluation_likeon_vue(
			@RequestBody CourseEvaluationLikeVO vo,
			HttpSession session
			) {
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			vo.setMember_id(member_id);
			eService.evalLikeOn(vo);
			map=commonsListData(vo.getCurpage(), vo.getCourse_no(),member_id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}

	// 강의평가 좋아요 취소
	@DeleteMapping("/enrollment/evaluation_likeoff_vue")
	public ResponseEntity evaluation_likeoff_vue(
			@RequestParam("ce_no") int ce_no,
			@RequestParam("curpage") int curpage,
			@RequestParam("course_no") int course_no,
			HttpSession session
			) {
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			eService.evalLikeOff(ce_no,member_id);
			map=commonsListData(curpage, course_no,member_id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	

}
