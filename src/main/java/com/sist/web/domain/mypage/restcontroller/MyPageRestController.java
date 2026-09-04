package com.sist.web.domain.mypage.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.domain.member.vo.MemberVO;
import com.sist.web.domain.mypage.service.*;
import com.sist.web.domain.mypage.vo.MyMemberVO;

import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class MyPageRestController {
	private final MyPageService mService;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/mypage/profile_update")
	public ResponseEntity<Map> mypage_profile_update(
			@RequestParam("member_id") int member_id){
		Map map=new HashMap();
		try {
			int eCount=mService.enrolledCount(member_id);
			MyMemberVO vo=mService.memberProfileData(member_id);
			map.put("eCount", eCount);
			map.put("vo", vo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
	
	// 프로필 수정
	@PostMapping("/mypage/profile_process")
	public ResponseEntity<Void> mypage_profile_process(MemberVO vo) {
	    // 1. 비밀번호 평문 가져오기
	    String rawPassword = vo.getPassword();
	    // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(rawPassword);
	    // 3. 암호화된 비밀번호를 VO에 다시 세팅 
	    vo.setPassword(encodedPassword);
	    // 4. DB에 저장 
	    //System.out.println("vo: "+vo);
	    mService.memberUpdateData(vo);
	    return ResponseEntity.status(HttpStatus.FOUND)
	    		             .location(URI.create("/mypage"))
	    		             .build();
	}
	
	
}
