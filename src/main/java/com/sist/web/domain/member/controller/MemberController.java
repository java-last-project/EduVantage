package com.sist.web.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import com.sist.web.domain.member.service.*;
import java.util.*;
import com.sist.web.domain.member.vo.*;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService mService;
	private final PasswordEncoder passwordEncoder;
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
	 @GetMapping("/member/id_check")
	 @ResponseBody
	 public int memberIdCheck(@RequestParam("username") String username) { 
	     return mService.memberIdCheck(username);
	 }
	@PostMapping("/member/join_process")
	public String member_join_process(MemberVO vo) {
	    // 1. 비밀번호 평문 가져오기
	    String rawPassword = vo.getPassword();
	    // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(rawPassword);
	    // 3. 암호화된 비밀번호를 VO에 다시 세팅 
	    vo.setPassword(encodedPassword);
	    // 4. DB에 저장 
	    mService.memberInsertData(vo);
	    
	    return "redirect:/member/login";
	}
}
