package com.sist.web.global.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.sist.web.global.security.*;
import com.sist.web.global.security.*;

import lombok.RequiredArgsConstructor;

@Configuration // xml => 자바 (설정을 쉽게) => 보안
@EnableWebSecurity // Security => 인터셉트
@RequiredArgsConstructor // lombok => 생성자를 통해 @Autowired

public class SecurityConfig {
   private final LoginSuccessHandler loginSuccessHandler;
   private final LoginFailHandler  loginFailHandler;
   private final DataSource dataSource;
   
   // 접근 권한 => SecurityFilterChain

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http)
   throws Exception
   {
	   // 공격 방어 
	   http
	    .csrf(csrf-> csrf.disable())
	    // 접근 권한 설정 (URL)
	    .authorizeHttpRequests(auth-> auth
	          .requestMatchers("/","/member/**").permitAll()
	          // 로그인 없이 접근 가능
	          .requestMatchers("/admin/**").hasRole("ADMIN")
	          // ADMIN 권한이 있는 사람만 접근이 가능
	          .anyRequest().permitAll()
	          // 지정이 안된 URL 주소
	    )
	    // 로그인 설정 => login_ok

	    .formLogin(form -> form 
	          .loginPage("/member/login")
	          // 로그인 화면창 설정 => 설정이 없는 경우 default login
	          .loginProcessingUrl("/member/login_process")
	          // 로그인 처리를 담당하는 URL => 가상으로 => Security에서 인터셉트가 가능

	          .usernameParameter("userid")
	          .passwordParameter("userpwd")
	          // => 로그인 처리를 위해 id, pwd를 전송

	          .defaultSuccessUrl("/",false)
	          .successHandler(loginSuccessHandler)
	          .failureHandler(loginFailHandler)
	          .permitAll() 
	    )
	    // 자동 로그인
	    .rememberMe(remember-> remember
	         .key("my-secret-key")
	         .rememberMeParameter("remember-me")
	         
	         .tokenValiditySeconds(60*60*24) 
	         // 저장 기간 : 1일 하루
	         .tokenRepository(persistentTokenRepository()) 
	         // persistent_logins 테이블에 저장

	         
	    )

	    
	    .logout(logout -> logout 
	          .logoutUrl("/member/logout")
	          .logoutSuccessUrl("/")
	          .invalidateHttpSession(true)
	          .deleteCookies("remember-me","JSESSIONID")
	    );
	    // remember-me
	    return http.build();
	    
   }

   // 인증 관리자 
   @Bean
   public AuthenticationManager authenticationManager(
      HttpSecurity http,
      BCryptPasswordEncoder passwordEncoder
   ) throws Exception
   {
	   AuthenticationManagerBuilder builder=
			   http.getSharedObject(AuthenticationManagerBuilder.class);
	   builder
	     .userDetailsService(jdbcUserDetailsService())
	     .passwordEncoder(passwordEncoder());
	   return builder.build();
   }
   @Bean
   public JdbcUserDetailsManager jdbcUserDetailsService() {
       JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
       
       // 1. 회원 정보 조회 
       manager.setUsersByUsernameQuery(
           "SELECT USERNAME as username, PASSWORD as password, ENABLED as enabled "
           + "FROM MEMBER WHERE USERNAME = ?"
       );
       
       // 2. 권한 조회
       manager.setAuthoritiesByUsernameQuery(
           "SELECT m.USERNAME as username, a.AUTHORITY as authority "
           + "FROM MEMBER m JOIN AUTHORITY a ON m.MEMBER_ID = a.MEMBER_ID " 
           + "WHERE m.USERNAME = ?"
       );
       
       return manager;
   }

   @Bean
   public BCryptPasswordEncoder passwordEncoder() {
	   return new BCryptPasswordEncoder();
   }
   @Bean
   public PersistentTokenRepository persistentTokenRepository() {
       JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
       repo.setDataSource(dataSource);
       return repo;
   }  
}

