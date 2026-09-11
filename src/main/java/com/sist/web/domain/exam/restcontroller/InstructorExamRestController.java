package com.sist.web.domain.exam.restcontroller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sist.web.domain.exam.dto.SubjectiveGradeRequest;
import com.sist.web.domain.exam.service.ExamGradingService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/instructor/exam/grading")
public class InstructorExamRestController {
    private final ExamGradingService gradingService;

    @GetMapping("/pending")
    public List<Map<String, Object>> pendingAnswers(HttpSession session) {
        return gradingService.getPendingSubjectiveList(requireInstructorId(session));
    }

    @PostMapping("/{answerNo}/claim")
    public ResponseEntity<Void> claimAnswer(@PathVariable int answerNo, HttpSession session) {
        boolean claimed = gradingService.claimTask(answerNo, requireInstructorId(session));
        return claimed ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/{answerNo}/claim")
    public ResponseEntity<Void> releaseAnswer(@PathVariable int answerNo, HttpSession session) {
        boolean released = gradingService.releaseClaim(answerNo, requireInstructorId(session));
        return released ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{answerNo}/grade")
    public ResponseEntity<Void> gradeAnswer(
            @PathVariable int answerNo,
            @RequestBody SubjectiveGradeRequest request,
            HttpSession session) {
        if (request.getScore() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "점수는 0 이상이어야 합니다.");
        }
        gradingService.gradeSubjective(
                answerNo,
                requireInstructorId(session),
                request.getScore());
        return ResponseEntity.noContent().build();
    }

    private int requireInstructorId(HttpSession session) {
        Object memberId = session.getAttribute("member_id");
        Object role = session.getAttribute("role");
        if (memberId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (!"ROLE_INSTRUCTOR".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return Integer.parseInt(String.valueOf(memberId));
    }
}
