package com.sist.web.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	 // 로그인
	 @RequestMapping("/member/login")
	 public String member_login(Model model)
	 {
		 model.addAttribute("main_html", "member/login");
		 return "main/main";
	 }
	 
	 // 회원가입
	 @RequestMapping("/member/join")
	 public String member_join(Model model) {
		 model.addAttribute("main_html", "member/join");
		 return "main/main";
	 }
}
