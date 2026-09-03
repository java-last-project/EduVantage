package com.sist.web.domain.community.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.domain.community.service.FreeBoardService;
import com.sist.web.domain.community.vo.FreeBoardVO;

import java.util.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FreeBoardRestController {
	private final FreeBoardService fService;
	
	@GetMapping("/freeboard/list_vue")
	public ResponseEntity<Map<String,Object>> freeboard_list_vue(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="fd",required=false)String fd){
		Map<String,Object> map=new HashMap<>();
		try {
			List<FreeBoardVO> list=fService.freeBoardList(page,fd);
			int[] pages=fService.freeBoardPageData(page,fd);
			String[] tags= {"curpage","totalpage","startPage","endPage","count"};
			map.put("list", list);
			for(int i=0;i<pages.length;i++) {
				map.put(tags[i], pages[i]);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(map);
	}
}
