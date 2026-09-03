package com.sist.web.global.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import com.sist.web.domain.member.mapper.*;
import com.sist.web.domain.member.vo.*;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler{
	private final MemberMapper mapper;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		try {
            // 1. DB에서 회원 정보 가져오기
            MemberVO vo = mapper.memberInfoData(authentication.getName());
            
            // 2. 안전장치: 데이터가 정상적으로 들어왔을 때만 세션에 담기
            if (vo != null) {
                HttpSession session = request.getSession();
                session.setAttribute("member_id", vo.getMember_id());
                session.setAttribute("username", vo.getUsername());
                session.setAttribute("member_id", vo.getMember_id());
                session.setAttribute("name", vo.getName());
                session.setAttribute("sex", vo.getSex());
                System.out.println(" 로그인 성공! 세션 저장 완료: " + vo.getUsername());
            } else {
                // 데이터가 비어있다면 콘솔에 경고창 띄우기
                System.out.println("DB에서 회원 정보를 찾지 못했습니다. 아이디: " + authentication.getName());
            }
            
        } catch (Exception e) {
            // 에러가 나더라도 화면이 멈추지 않도록 예외 처리
            System.out.println(" 세션 저장 중 백엔드 에러 발생");
            e.printStackTrace();
        }
        
        // 3. 에러 유무와 상관없이 무조건 메인 화면으로 강제 이동 
        response.sendRedirect("/");
    }


}