package com.sist.web.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	 public String member_join_process(MemberVO vo, @RequestParam("passwordConfirm") String passwordConfirm,
	         RedirectAttributes ra) {
	     String rawPassword = vo.getPassword();

	     // 비밀번호 길이 재검증
	     if (rawPassword == null || rawPassword.length() < 8) {
	         ra.addFlashAttribute("message", "비밀번호는 8자 이상 입력해야 합니다.");
	         return "redirect:/member/join";
	     }
	     // 비밀번호 일치 재검증
	     if (!rawPassword.equals(passwordConfirm)) {
	         ra.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");
	         return "redirect:/member/join";
	     }
	     // 아이디 중복 재검증
	     if (mService.memberIdCheck(vo.getUsername()) > 0) {
	         ra.addFlashAttribute("message", "이미 사용 중인 아이디입니다.");
	         return "redirect:/member/join";
	     }

	     // 비밀번호 암호화 
	     String encodedPassword = passwordEncoder.encode(rawPassword);
	     vo.setPassword(encodedPassword);
	     mService.memberInsertData(vo);

	     return "redirect:/member/login";
	 }
}
