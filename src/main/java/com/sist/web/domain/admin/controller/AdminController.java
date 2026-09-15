package com.sist.web.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sist.web.domain.admin.service.AdminService;
import com.sist.web.domain.community.service.NoticeBoardService;
import com.sist.web.domain.community.vo.NoticeBoardVO;
import com.sist.web.domain.course.vo.CourseVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class AdminController 
{
	private final AdminService aService;
	private final NoticeBoardService nService;
	
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
	
	@GetMapping("/admin/order_course")
	public String admin_order_course(@RequestParam(value="page", required = false) String page,
							@RequestParam(value="order_status", required = false) String order_status,
							Model model)
	{
		if(page==null) page="1";
		
		int curpage = Integer.parseInt(page);
		int count = aService.adminCountCoursePayment(order_status);
		
		int totalpage = (int)(Math.ceil(count/10.0));
		final int BLOCK = 10;
		int startPage = ((curpage-1)/BLOCK*BLOCK)+1;
		int endPage = ((curpage-1)/BLOCK*BLOCK)+BLOCK;
		
		List<Map<String,Object>> list = aService.adminCoursePaymentListData(curpage, order_status);
		
		if(endPage>totalpage)
			endPage = totalpage;
		
		int startNum = (curpage-1) * 10 + 1;
		int endNum = curpage * 10 > count ? count : curpage * 10;
		
		model.addAttribute("startNum", startNum);
		model.addAttribute("endNum", endNum);
		model.addAttribute("count", count);
		
		model.addAttribute("list", list);
		model.addAttribute("curpage", curpage);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		model.addAttribute("order_status", order_status);
		
		model.addAttribute("admin_html", "admin/order_course");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@PostMapping("/admin/order_course_cancel")
	public String admin_order_course_cancle(@RequestParam("no") int no, Model model)
	{
		aService.adminPaymentCancleCourse(no);
		
		return "redirect:/admin/order_course";
	}
	
	@GetMapping("/admin/order_book")
	public String admin_order_book(@RequestParam(value="page", required = false) String page,
							@RequestParam(value="order_status", required = false) String order_status,
							Model model)
	{
		if(page==null) page="1";
		
		int curpage = Integer.parseInt(page);
		int count = aService.adminOrderBookCount(order_status);
		
		int totalpage = (int)(Math.ceil(count/10.0));
		final int BLOCK = 10;
		int startPage = ((curpage-1)/BLOCK*BLOCK)+1;
		int endPage = ((curpage-1)/BLOCK*BLOCK)+BLOCK;
		
		List<Map<String,Object>> list = aService.adminOrderBookListData(curpage, order_status);
		
		if(endPage>totalpage)
			endPage = totalpage;
		
		int startNum = (curpage-1) * 10 + 1;
		int endNum = curpage * 10 > count ? count : curpage * 10;
		
		model.addAttribute("startNum", startNum);
		model.addAttribute("endNum", endNum);
		model.addAttribute("count", count);
		
		model.addAttribute("list", list);
		model.addAttribute("curpage", curpage);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		model.addAttribute("order_status", order_status);
		
		model.addAttribute("admin_html", "admin/order_book");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/order_book_detail")
	public String admin_order_book_detail(@RequestParam("no") int no, Model model)
	{
		List<Map<String, Object>> list = aService.adminPaymentDetailListData(no);
		
		model.addAttribute("list", list);
		
		model.addAttribute("admin_html", "admin/order_book_detail");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@PostMapping("/admin/order_book_cancel")
	public String admin_order_book_cancle(@RequestParam("no") int no, Model model)
	{
		aService.adminPaymentCancleBook(no);
		
		return "redirect:/admin/order_book";
	}
	
	@GetMapping("/admin/notice")
	public String admin_notice(@RequestParam(value="page", required = false) String page,
							@RequestParam(value="fd", required = false) String fd,
							@RequestParam(value="categoryNo", required = false) Integer categoryNo,
							Model model)
	{
		if(page==null) page="1";
		if(categoryNo==null) categoryNo=0;
		
		List<NoticeBoardVO> list = nService.noticeBoardList(Integer.parseInt(page), fd, categoryNo);
		Map<String, Object> pages = nService.noticeBoardPage(Integer.parseInt(page), fd, categoryNo);
        
		
		int startNum = (Integer.parseInt(page)-1) * 20 + 1;
		int endNum = Integer.parseInt(page) * 20 > (int)pages.get("count") ? (int)pages.get("count") : Integer.parseInt(page) * 20;
		
		model.addAttribute("startNum", startNum);
		model.addAttribute("endNum", endNum);
		
        model.addAttribute("list", list);
        model.addAttribute("curpage", pages.get("curpage"));
        model.addAttribute("totalpage", pages.get("totalpage"));
        model.addAttribute("startPage", pages.get("startPage"));
        model.addAttribute("endPage", pages.get("endPage"));
        model.addAttribute("count", pages.get("count"));
        
        if(fd!=null)
        	model.addAttribute("fd",fd);
        if(categoryNo!=null)
        	model.addAttribute("category_no", categoryNo);
		
		model.addAttribute("admin_html", "admin/notice");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/notice_insert")
	public String admin_notice_insert(Model model)
	{
		model.addAttribute("admin_html", "admin/notice_insert");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/qna")
	public String admin_qna(@RequestParam(value="page", required = false) String page, 
					@RequestParam(value="categoryno", required = false) String categoryno, 
					@RequestParam(value="status", required = false) String status, 
					Model model)
	{
		if(page==null) page="1";
		if(categoryno==null) categoryno="-1";
		if(status==null) status="all";
		
		int curpage = Integer.parseInt(page);
		int count = aService.adminQnaCount(Integer.parseInt(categoryno), status);
		
		int totalpage = (int)(Math.ceil(count/10.0));
		final int BLOCK = 10;
		int startPage = ((curpage-1)/BLOCK*BLOCK)+1;
		int endPage = ((curpage-1)/BLOCK*BLOCK)+BLOCK;
		
		List<Map<String, Object>> list = aService.adminQnaListData(curpage, Integer.parseInt(categoryno), status);
		
		if(endPage>totalpage)
			endPage = totalpage;
		
		int startNum = (curpage-1) * BLOCK + 1;
		int endNum = curpage * BLOCK > count ? count : curpage * BLOCK;
		
		model.addAttribute("startNum", startNum);
		model.addAttribute("endNum", endNum);
		
		model.addAttribute("list", list);
		model.addAttribute("curpage", curpage);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("count", count);
		
		model.addAttribute("categoryno", categoryno);
		model.addAttribute("status", status);
		
		model.addAttribute("admin_html", "admin/qna");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@GetMapping("/admin/qna_detail")
	public String admin_qna_detail(@RequestParam("no") int no,Model model)
	{
		Map<String, Object> vo = aService.adminQnaDetailData(no);
		
		model.addAttribute("vo", vo);
		
		model.addAttribute("admin_html", "admin/qna_detail");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
	
	@PostMapping("/admin/qna_answer")
	public String admin_qna_answer(@RequestParam("no") int no, @RequestParam("content") String content, HttpSession session, Model model)
	{
		int member_id = (int)session.getAttribute("member_id");
		
		aService.adminQnaAnswerInsert(member_id, no, content);
		
		return "redirect:qna_detail?no="+no;
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

	@GetMapping("/admin/exam")
	public String admin_exam(@RequestParam(value="page", required = false) String page, Model model)
	{
		if(page==null) page="1";
		
		int curpage = Integer.parseInt(page);
		int count = aService.adminExamCount();
		
		int totalpage = (int)(Math.ceil(count/10.0));
		final int BLOCK = 10;
		int startPage = ((curpage-1)/BLOCK*BLOCK)+1;
		int endPage = ((curpage-1)/BLOCK*BLOCK)+BLOCK;
		
		List<Map<String, Object>> list = aService.adminExamListData(curpage);
		
		if(endPage>totalpage)
			endPage = totalpage;
		
		int startNum = (curpage-1) * BLOCK + 1;
		int endNum = curpage * BLOCK > count ? count : curpage * BLOCK;
		
		model.addAttribute("startNum", startNum);
		model.addAttribute("endNum", endNum);
		
		model.addAttribute("list", list);
		model.addAttribute("curpage", curpage);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("count", count);
		
		model.addAttribute("admin_html", "admin/exam");
		model.addAttribute("main_html", "admin/main");
		return "main/main";
	}
}
