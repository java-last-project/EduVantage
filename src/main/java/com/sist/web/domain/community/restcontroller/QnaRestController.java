package com.sist.web.domain.community.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.domain.community.service.QnaService;
import com.sist.web.domain.community.vo.QnaBoardVO;

import java.util.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class QnaRestController {
	private final QnaService qService;
	
	@GetMapping("/qna/list_vue")
	public ResponseEntity<Map<String,Object>> qnaList_vue(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="fd",required=false)String fd,@RequestParam(value="categoryNo",required=false)Integer categoryNo){
		Map<String,Object> map=new HashMap<>();
		if("".equals(fd)) {
			fd=null;
		}
		if(categoryNo!=null&&categoryNo==0) {
			categoryNo=null;
		}
		try {
			map=qService.qnaPageData(page, fd, categoryNo);
			List<QnaBoardVO> list=qService.qnaListData(page, fd, categoryNo);
			map.put("list", list);
			return ResponseEntity.ok(map);
		}catch(Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
