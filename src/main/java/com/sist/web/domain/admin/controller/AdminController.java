package com.sist.web.domain.admin.controller;

import org.apache.naming.StringManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sist.web.domain.admin.service.AdminService;
import com.sist.web.domain.course.vo.CourseVO;
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
	public String admin_member_detail(@RequestParam("member_id") int member_id, Model model)
	{
		Map<String, Object> vo = aService.adminMemberDetailData(member_id);
		

		model.addAttribute("vo", vo);
		
		model.addAttribute("admin_html", "admin/member_detail");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/member_update_enabled")
	public String admin_member_update_enabled(
			@RequestParam("member_id") String member_id, 
			@RequestParam("enabled") int enabled, 
			Model model)
	{
		aService.adminUpdateMemberEnabled(enabled, Integer.parseInt(member_id));
		return "redirect:/admin/member_detail?member_id="+member_id;
	}
	
	@GetMapping("/admin/course")
	public String admin_course(@RequestParam(value="page", required = false) String page,
								@RequestParam(value="title", required = false) String title,
								Model model)
	{
		if(page==null) page = "1";
		List<Map<String, Object>> list = null;
		
		int pageNum = Integer.parseInt(page);
		int count = 0;
		
		// 강의명 값이 들어오면 검색모드
		if(title != null && !title.isEmpty())
		{
			list = aService.adminFindCourseListData(title, pageNum);
			count = aService.adminGetCountFindCourse(title);
			model.addAttribute("title", title);
		}
		else
		{
			list = aService.adminCourseListData(pageNum);			
			count = aService.adminGetCountCourse();
		}
		
		int totalpage = (int)(Math.ceil(count/10.0));
		final int BLOCK = 10;
		int startPage = ((pageNum-1)/BLOCK*BLOCK)+1;
		int endPage = ((pageNum-1)/BLOCK*BLOCK)+BLOCK;
		
		if(endPage>totalpage)
			endPage = totalpage;
		
		
		int startNum = (pageNum-1) * 10 + 1;
		int endNum = pageNum * 10 > count ? count : pageNum * 10;
		
		model.addAttribute("startNum", startNum);
		model.addAttribute("endNum", endNum);
		
		model.addAttribute("list", list);
		model.addAttribute("curpage", pageNum);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		model.addAttribute("count", count);
		
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
		int memberCount = aService.getTotalMember();
		int courseCount = aService.adminGetCountCourse();
		int instCount = aService.adminGetTotalInstCount();
		List<CourseVO> list = aService.adminGetBest5Course();
		
		// 화면 프로그래스 바 출력용 데이터 구하기 
		int maxCount = list.get(0).getStudent_count();
		
		List<Map<String, Object>> bestCounts = new ArrayList<>();
		for(CourseVO vo : list)
		{
			int percent = Math.round((vo.getStudent_count() * 100f) / maxCount);
			
			Map<String, Object> map = new HashMap<>();
			map.put("no", vo.getNo());
			map.put("title", vo.getTitle());
			map.put("student_count", vo.getStudent_count());
			map.put("percent", percent);
			
			bestCounts.add(map);
		}
		
		
		model.addAttribute("memberCount", memberCount);
		model.addAttribute("courseCount", courseCount);
		model.addAttribute("instCount", instCount);
		model.addAttribute("list", bestCounts);
		
		model.addAttribute("admin_html", "admin/dashboard");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}

}
