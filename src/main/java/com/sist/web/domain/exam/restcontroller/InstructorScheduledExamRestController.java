package com.sist.web.domain.exam.restcontroller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sist.web.domain.exam.service.InstructorScheduledExamService;
import com.sist.web.domain.exam.vo.ExamQuestionVO;
import com.sist.web.domain.exam.vo.ScheduledExamVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/instructor/exam/scheduled")
public class InstructorScheduledExamRestController {
	private final InstructorScheduledExamService scheduledExamService;

	@GetMapping("/list")
	public List<ScheduledExamVO> scheduledExamList(HttpSession session){
		return scheduledExamService.getScheduledExamList(requireInstructorId(session));
	}

	@GetMapping("/{examNo}")
	public ScheduledExamVO scheduledExamDetail(@PathVariable int examNo,HttpSession session){
		return scheduledExamService.getScheduledExamDetail(requireInstructorId(session),examNo);
	}

	@GetMapping("/questions")
	public List<ExamQuestionVO> questionBank(
			@RequestParam(value="theme",required=false) Integer theme,
			HttpSession session){
		requireInstructorId(session);
		return scheduledExamService.getQuestionBank(theme);
	}

	@PostMapping
	public ResponseEntity<?> insertScheduledExam(@RequestBody ScheduledExamVO vo,HttpSession session){
		int examNo=scheduledExamService.insertScheduledExam(requireInstructorId(session),vo);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("examNo",examNo));
	}

	@PutMapping("/{examNo}")
	public ResponseEntity<Void> updateScheduledExam(
			@PathVariable int examNo,
			@RequestBody ScheduledExamVO vo,
			HttpSession session){
		scheduledExamService.updateScheduledExam(requireInstructorId(session),examNo,vo);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{examNo}")
	public ResponseEntity<Void> deleteScheduledExam(@PathVariable int examNo,HttpSession session){
		scheduledExamService.deleteScheduledExam(requireInstructorId(session),examNo);
		return ResponseEntity.noContent().build();
	}

	private int requireInstructorId(HttpSession session){
		Object memberId=session.getAttribute("member_id");
		Object role=session.getAttribute("role");
		if(memberId==null){
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		if(!"ROLE_INSTRUCTOR".equals(role)){
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		return Integer.parseInt(String.valueOf(memberId));
	}
}
