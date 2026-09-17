package com.sist.web.domain.mypage.restcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.util.stream.Collectors;

import com.sist.web.domain.enrollment.vo.CourseEnrollmentVO;
import com.sist.web.domain.exam.vo.RecommendCourseVO;
import com.sist.web.domain.mypage.service.MyPageRecommendService;
import com.sist.web.domain.mypage.service.MyPageService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MyPageRecommendRestController {
	private final MyPageService mService;
	private final MyPageRecommendService rService;
	
	@GetMapping("/mypage/recommend_courses")
	public ResponseEntity<Map> remommendCourses(HttpSession session) {
		Map map=new HashMap();
		try {
			int member_id=(int)session.getAttribute("member_id");
			List<Integer> enrolledCourseNos=mService.mypageCourseListData(member_id)
					.stream().map(CourseEnrollmentVO::getCourse_no)
					.collect(Collectors.toList());
			List<RecommendCourseVO> list=rService.courseRecomandListData(enrolledCourseNos);
			map.put("rList", list);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(map);
	}
}
