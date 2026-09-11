package com.sist.web.domain.exam.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;

@Controller
public class InstructorExamController {

    @GetMapping("/instructor/exam/grading")
    public String gradingPage(HttpSession session, Model model) {
        requireInstructor(session);
        model.addAttribute("instructor_html", "instructor/exam/grading");
        model.addAttribute("main_html", "instructor/main");
        return "main/main";
    }

    private void requireInstructor(HttpSession session) {
        if (!"ROLE_INSTRUCTOR".equals(session.getAttribute("role"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
