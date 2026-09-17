package com.sist.web.global.security;


import com.sist.web.domain.member.mapper.MemberMapper;
import com.sist.web.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email"); // 구글 계정 이메일
        String name = (String) attributes.get("name");   // 구글 계정 이름

        // 1. DB에서 해당 이메일(username)로 가입된 회원이 있는지 조회
        MemberVO vo = memberMapper.memberInfoData(email);

        // 2. 만약 처음 구글 로그인한 회원이라면, DB에 자동으로 회원가입 처리
        if (vo == null) {
            MemberVO newMember = new MemberVO();
            newMember.setUsername(email); // 구글 이메일을 아이디로 사용
            newMember.setPassword(passwordEncoder.encode("GOOGLE_SOCIAL_USER")); // 소셜 로그인용 임시 비밀번호 암호화
            newMember.setName(name != null ? name : "구글사용자");
            newMember.setSex("M"); // DB 성별 컬럼 제약조건에 맞춰 설정 (필요시 수정)
            newMember.setBirthdate("20000101");
            newMember.setPhone("010-0000-0000");
            newMember.setPost("00000");
            newMember.setAddr1("소셜로그인");
            newMember.setAddr2("소셜로그인");
            newMember.setProfile_desc("구글 소셜 로그인 계정입니다.");

            // 회원 정보 INSERT
            memberMapper.memberInsertData(newMember);
            // 권한(ROLE_USER) INSERT
            memberMapper.memberAuthInsert(email);

            // 가입 직후 다시 회원 정보 조회
            vo = memberMapper.memberInfoData(email);
        }

        // 3. 기존 LoginSuccessHandler와 똑같이 세션에 회원 정보 저장 (NullPointerException 방지)
        if (vo != null) {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest().getSession();
            
            session.setAttribute("member_id", vo.getMember_id());
            session.setAttribute("username", vo.getUsername());
            session.setAttribute("name", vo.getName());
            session.setAttribute("sex", vo.getSex());
            session.setAttribute("role", "ROLE_USER");
            
            System.out.println(" 구글 소셜 로그인 성공 및 세션 저장 완료: " + vo.getUsername());
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email"
        );
    }
}