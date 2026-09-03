package com.sist.web.domain.course.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import com.sist.web.domain.course.service.CourseService;
import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.course.vo.TechStackVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CourseRestController {
	private final CourseService cService;
	
	@GetMapping("/course/list_vue")
	public ResponseEntity<Map<String,Object>> course_list_vue(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="column",defaultValue="no")String column,@RequestParam(value="category",required=false)String category,@RequestParam(value="fd",required=false)String fd){
		Map<String,Object> map=new HashMap<>();
		try {
			List<CourseVO> list=cService.courseListData(page, column, category,fd);
			int[] pages=cService.coursePageData(page, category,fd);
			List<TechStackVO> catList=cService.courseCategoryList();
			map.put("cList", list);
			map.put("catList", catList);
			map.put("curpage", pages[0]);
			map.put("totalpage", pages[1]);
			map.put("startPage", pages[2]);
			map.put("endPage", pages[3]);
			map.put("count", pages[4]);
		}catch(Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(map);
	}
}
