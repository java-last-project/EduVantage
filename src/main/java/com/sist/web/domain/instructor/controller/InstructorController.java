package com.sist.web.domain.instructor.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.vo.CourseQnaReplyVO;
import com.sist.web.domain.enrollment.vo.CourseQnaVO;
import com.sist.web.domain.instructor.service.InstructorService;
import com.sist.web.domain.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class InstructorController {
	private final InstructorService iService;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/instructor/course")
	public String instructor_course(HttpSession session, Model model)
	{
		int member_id = (int)session.getAttribute("member_id");
		List<Map<String, Object>> list = iService.InstCourseDataList(member_id);
		
		model.addAttribute("list", list);
		
		model.addAttribute("instructor_html", "instructor/course");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
	
	@GetMapping("/instructor/course_detail")
	public String instructor_course_detail(HttpSession session, @RequestParam("course_no") int course_no, Model model)
	{
		//int member_id = (int)session.getAttribute("member_id");
		 
		List<Map<String,Object>> sList = iService.InstCourseEnrollStudList(course_no);
		CourseVO vo = iService.InstCourseDetailData(course_no);
		
		model.addAttribute("vo", vo);
		model.addAttribute("sList", sList);
		
		model.addAttribute("instructor_html", "instructor/course_detail");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
	
	@GetMapping("/instructor/news")
	public String instructor_news(@RequestParam("courseId") int courseId, Model model)
	{
	    List<Map<String, Object>> list = iService.instCourseNewsListData(courseId);
	    
	    // 첨부파일 구별용 문자 제거해서 화면에 출력
	    for(Map<String, Object> map : list)
	    {
	    	Object obj = map.get("FILENAME");
	    	
	    	if(obj != null)
	    	{
	    		String filename = obj.toString();
	    		
	    		String originalFilename = filename.substring(filename.indexOf("_") + 1);
	    		
	    		map.put("filename", originalFilename);
	    	}
	    }
	    
	    model.addAttribute("list", list);
	    model.addAttribute("courseId", courseId);
	    model.addAttribute("instructor_html", "instructor/news");
	    model.addAttribute("main_html", "instructor/main");
	    return "main/main";
	}

	@GetMapping("/instructor/news_insert")
	public String instructor_news_insert_form(@RequestParam("courseId") int courseId, Model model)
	{
	    model.addAttribute("courseId", courseId);
	    model.addAttribute("instructor_html", "instructor/news_insert");
	    model.addAttribute("main_html", "instructor/main");
	    return "main/main";
	}

	@PostMapping("/instructor/news_insert")
	public String instructor_news_insert(
	        @RequestParam("courseId") int courseId,
	        @RequestParam("subject") String subject,
	        @RequestParam("content") String content,
	        @RequestParam(value="file", required = false) MultipartFile file)
	{
		String filename = null;
		Long filesize = null;
		
		// 파일이 업로드 될 경우 파일 이름과 파일 사이즈를 받아옴
		if(file!=null && !file.isEmpty())
		{
			// 파일이름 : currentTimeMillis() 는 이름 중복 방지용
			filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			filesize = file.getSize();
			
			// 파일 업로드 경로 지정
			String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "courseNews" + File.separator;
			
			File dir = new File(uploadDir);
			
			if(!dir.exists()) 
				dir.mkdirs();
			
			try {
				File dest = new File(uploadDir + filename);
				file.transferTo(dest);
				
			} catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
				filename = null;
				filesize = null;
			}
			
			iService.instCourseNewsInsert(courseId, subject, content, filename, filesize);
		}
		else
		{
			
			iService.instCourseNewsInsert(courseId, subject, content);	// filename, filesize 추가해서 보내주는거 만들어야함
		}
		
	    return "redirect:/instructor/news?courseId=" + courseId;
	}
	
	@GetMapping("/instructor/news_detail")
	public String instructor_news_detail(@RequestParam("no") int no, Model model)
	{
	    iService.instCourseNewsHitUp(no);
	    Map<String, Object> vo = iService.instCourseNewsDetail(no);
	    
	 // 첨부파일 구별용 문자 제거해서 화면에 출력
	    Object obj = vo.get("FILENAME");
	    
	    if(obj != null)
	    {
	    	String filename = obj.toString();
	    	
	    	String originalFilename = filename.substring(filename.indexOf("_") + 1);
	    	
	    	vo.put("filename", originalFilename);
	    }
	    
	    model.addAttribute("vo", vo);
	    model.addAttribute("instructor_html", "instructor/news_detail");
	    model.addAttribute("main_html", "instructor/main");
	    return "main/main";
	}
	
	@GetMapping("/instructor/news_download")
	public void instructor_news_download(@RequestParam("filename") String filename, HttpServletResponse response) throws IOException
	{
		String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "courseNews" + File.separator;
		File file = new File(uploadDir + filename);
		
		if(!file.exists())
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// 원본 파일명 복원
		String originalFilename = filename.substring(filename.indexOf("_") + 1);
		String encodeFilename = URLEncoder.encode(originalFilename, "UTF-8").replaceAll("\\+", "%20");
		
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + encodeFilename + "\"");
		response.setContentLengthLong(file.length());
		
		try (FileInputStream fis = new FileInputStream(file);
		         OutputStream os = response.getOutputStream()) 
			{
		        FileCopyUtils.copy(fis, os);
		    }
	}
	
	@GetMapping("/instructor/course_edit")
	public String instructor_course_edit(HttpSession session, @RequestParam("course_no") int course_no, Model model)
	{
		CourseVO vo = iService.InstCourseDetailData(course_no);
		
		model.addAttribute("vo", vo);
		
		model.addAttribute("instructor_html", "instructor/course_edit");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
	
	@PostMapping("/instructor/course_edit_update")
	public String instructor_course_edit_update(@ModelAttribute("vo") CourseVO vo, Model model)
	{
		iService.instUpdateCourseData(vo);
		return "redirect:/instructor/course_detail?course_no="+vo.getNo();
	}
	
	@GetMapping("/instructor/new_course")
	public String instructor_new_course(Model model)
	{
		model.addAttribute("instructor_html", "instructor/new_course");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
	
	@PostMapping("/instructor/new_course_insert")
	public String instructor_new_course_insert(HttpSession session, @ModelAttribute("vo") CourseVO vo, Model model)
	{
		int member_id=(int)session.getAttribute("member_id");
		vo.setInstructor_no(member_id);
		iService.instInsertNewCourse(vo);
		
		return "redirect:/instructor/course";
	}
	
	@GetMapping("/instructor/profile")
	public String instructor_profile(HttpSession session, Model model)
	{
		int member_id=(int)session.getAttribute("member_id");
		MemberVO vo = iService.InstProfileData(member_id);
		
		model.addAttribute("vo", vo);
		
		model.addAttribute("instructor_html", "instructor/profile");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
	
	@GetMapping("/instructor/profile_edit")
	public String instructor_profile_edit(HttpSession session, Model model)
	{
		int member_id=(int)session.getAttribute("member_id");
		MemberVO vo = iService.InstProfileData(member_id);
		
		model.addAttribute("vo", vo);
		
		model.addAttribute("instructor_html", "instructor/profile_edit");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
	
	@PostMapping("/instructor/profile_edit_ok")
	public String instructor_profile_edit_ok(HttpSession session,
					RedirectAttributes redirectAttributes,
					@RequestParam(value = "password", required = false) String password,
					@RequestParam(value = "password_confirm", required = false) String password_confirm,
					@RequestParam("name") String name,
					@RequestParam("sex") String sex,
					@RequestParam("birthdate") String birthdate,
					@RequestParam("phone") String phone,
					@RequestParam("email") String email,
					@RequestParam("post") String post,
					@RequestParam("addr1") String addr1,
					@RequestParam("addr2") String addr2,
					@RequestParam("profile_desc") String profile_desc
					)
	{
		int member_id = (int)session.getAttribute("member_id");
		MemberVO vo = new MemberVO();
		
		vo.setMember_id(member_id);
		vo.setName(name);
		vo.setSex(sex);
		vo.setBirthdate(birthdate);
		vo.setPhone(phone);
		vo.setEmail(email);
		vo.setPost(post);
		vo.setAddr1(addr1);
		vo.setAddr2(addr2);
		vo.setProfile_desc(profile_desc);
		vo.setPassword(null);
		
		// password가 들어오는 경우 비밀번호 변경
		if(password != null && !password.isEmpty() && password.equals(password_confirm))
		{
			// 비밀번호 암호화
			String encodePwd = passwordEncoder.encode(password);
			vo.setPassword(encodePwd);	// 암호화 시킨 비밀번호를 vo에 저장
		}
		else if(password != null && !password.isEmpty() && !password.equals(password_confirm))
		{
			// 비밀번호가 틀림
			vo.setPassword(null);
			redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
			return "redirect:/instructor/profile_edit";
		}
		else if(password == null || password.isEmpty())	// password가 들어오지 않는 경우 null을 넣고 기본 정보만 update
		{
			vo.setPassword(null);
		}
		
		iService.instProfileUpdate(vo);
		
		return "redirect:/instructor/profile";
	}
	
	@GetMapping("/instructor/qna")
	public String instructor_qna(
				@RequestParam(value = "page", required = false) String page,
				@RequestParam(value = "title", required = false) String title,
				@RequestParam(value = "status", required = false) String status,
				HttpSession session, Model model)
	{
		// title, status 아무것도 들어오지 않으면 전체 목록 출력
		// 들어오면 필터링 목록 출력
		int member_id = (int)session.getAttribute("member_id");
		final int BLOCK = 10;
		int count = 0;
		List<Map<String, Object>> list = null;
	
		if(page == null) page = "1";
		
		int curpage = Integer.parseInt(page);
		int startPage = ((curpage-1)/BLOCK*BLOCK)+1;
		int endPage = ((curpage-1)/BLOCK*BLOCK)+BLOCK;

		int start = (curpage - 1) * BLOCK;
		
		// 필터링이 들어오면 필터링된 갯수, 필터링이 들어오지 않으면 전체 갯수
		if(title==null && status==null)
		{
			count = iService.instCountQnaList(member_id);	
			list = iService.instQnaListData(member_id, start);
		}
		else
		{
			count = iService.instCountQnaList(member_id, title, status);
			list = iService.instQnaListData(member_id, start, title, status);
		}
		
		int totalpage = (int)(Math.ceil(count/10.0));
		
		if(endPage > totalpage) endPage = totalpage;
		
		List<String> filterList = iService.instQnaFilterCourse(member_id);	// 필터링에 사용할 강의 목록 출력
		
		int startNum = start + 1;
		int endNum = curpage * 10 > count ? count : curpage * 10;
		
		if(title==null) title="all";
		if(status==null) status="all";
		
		model.addAttribute("title", title);
		model.addAttribute("status", status);
		model.addAttribute("filterList", filterList);
		model.addAttribute("startNum", startNum);
		model.addAttribute("endNum", endNum);
		model.addAttribute("count", count);
		model.addAttribute("list", list);
		model.addAttribute("curpage", curpage);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		model.addAttribute("instructor_html", "instructor/qna");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
	
	@GetMapping("/instructor/qna_detail")
	public String instructor_qna_detail(HttpSession session, @RequestParam("no") int no, Model model)
	{
		Map<String, Object> map = iService.instQnaDetailData(no);	// QnA 질문 내용
		
		if(map.get("STATUS").equals("Y"))	// 답변완료일 경우 답변한 내용 노출
		{
			CourseQnaReplyVO ans = iService.instQnaAnswerData(no);
			model.addAttribute("ans", ans);
		}
		else if(map.get("STATUS").equals("N"))	// 답변대기일 경우 답변 내용 작성
		{
			
		}
		
		model.addAttribute("vo", map);
		
		model.addAttribute("instructor_html", "instructor/qna_detail");
		model.addAttribute("main_html", "instructor/main");
		return "main/main";
	}
	
	@PostMapping("/instructor/qna_answer")
	public String instructor_qna_answer(HttpSession session, 
				@RequestParam("no") int no,
				@RequestParam("answer") String answer,
				Model model)
	{
		int member_id = (int)session.getAttribute("member_id");
		CourseQnaReplyVO vo = new CourseQnaReplyVO();
		vo.setAnswer(answer);
		vo.setMember_id(member_id);
		vo.setCourse_qna_no(no);
		
		iService.instQnaAnswerInsert(vo, no);
		
		return "redirect:/instructor/qna_detail?no="+no;
	}
	
	
}
