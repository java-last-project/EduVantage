package com.sist.web.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sist.web.domain.admin.service.AdminService;
import com.sist.web.domain.member.vo.MemberVO;

import lombok.RequiredArgsConstructor;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class AdminController 
{
	private final AdminService aService;
	
	@GetMapping("/admin/member")
	public String admin_member(
			@RequestParam(value="page", required = false) String page,
			@RequestParam(value="name", required = false) String name,
			@RequestParam(value="enabled", required = false) String enabled,
			@RequestParam(value="authority", required = false) String authority,
			Model model)
	{
		// name, type, status 아무것도 들어오지 않으면 전체 목록 출력
		// name이 들어오면 검색
		// type, status가 들어오면 필터링된 목록 출력
		
		if(page==null) page = "1";
		
		List<Map<String,Object>> list = null;
		
		// 이름값이 들어오면 검색을 했다는 의미
		if(name!=null && !name.isEmpty())
		{
			list = aService.adminMemberFindByName(name);
			
			model.addAttribute("count", list.size());
			model.addAttribute("name", name);
			model.addAttribute("isSearch", true);
		}
		else if((enabled!=null && !enabled.isEmpty()) || (authority!=null && !authority.isEmpty()))	// 필터링 확인됨, 필터링된 목록만 출력
		{
			list = aService.adminMemberFilterListData(authority, enabled, Integer.parseInt(page));
			
			// 페이지 계산
			// TODO: 시간 나면 공통 함수로 묶어주기
			int[] pages = aService.getPageData(Integer.parseInt(page), authority, enabled);
			int count = aService.getCountFilterMember(authority, enabled);
			
			int startNum = (pages[0]-1) * 10 + 1;
			int endNum = pages[0] * 10 > count ? count : pages[0] * 10;
			
			model.addAttribute("startNum", startNum);
			model.addAttribute("endNum", endNum);
			model.addAttribute("curpage", pages[0]);
			model.addAttribute("totalpage", pages[1]);
			model.addAttribute("startPage", pages[2]);
			model.addAttribute("endPage", pages[3]);
			model.addAttribute("count", count);
			model.addAttribute("isSearch", false);
		}
		else	// 회원 목록 전체 출력
		{
			list = aService.adminMemberListData(Integer.parseInt(page));
			
			// 페이지 계산
			// TODO: 시간 나면 공통 함수로 묶어주기
			int[] pages = aService.getPageData(Integer.parseInt(page));
			int count = aService.getTotalMember();
			int startNum = (pages[0]-1) * 10 + 1;
			int endNum = pages[0] * 10 > count ? count : pages[0] * 10;
			
			model.addAttribute("startNum", startNum);
			model.addAttribute("endNum", endNum);
			model.addAttribute("curpage", pages[0]);
			model.addAttribute("totalpage", pages[1]);
			model.addAttribute("startPage", pages[2]);
			model.addAttribute("endPage", pages[3]);
			model.addAttribute("count", count);
			model.addAttribute("isSearch", false);
			
		}
		model.addAttribute("authority", authority);
		model.addAttribute("enabled", enabled);
		model.addAttribute("name", name);
		model.addAttribute("list", list);
		
		model.addAttribute("admin_html", "admin/member");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/member_detail")
	public String admin_member_detail(Model model)
	{
		model.addAttribute("admin_html", "admin/member_detail");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/course")
	public String admin_course(Model model)
	{
		model.addAttribute("admin_html", "admin/course");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/order")
	public String admin_order(Model model)
	{
		model.addAttribute("admin_html", "admin/order");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/notice")
	public String admin_notice(Model model)
	{
		model.addAttribute("admin_html", "admin/notice");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/qna")
	public String admin_qna(Model model)
	{
		model.addAttribute("admin_html", "admin/qna");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/dashboard")
	public String admin_dashboard(Model model)
	{
		model.addAttribute("admin_html", "admin/dashboard");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}

}
