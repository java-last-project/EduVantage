package com.sist.web.domain.community.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sist.web.domain.community.service.QnaService;
import com.sist.web.domain.community.vo.QnaBoardVO;
import com.sist.web.domain.community.vo.QnaReplyVO;

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
	
	@GetMapping("/qna/reply")
	public ResponseEntity<Map<String,Object>> qnaReplyData(@RequestParam("qnaNo")Integer qnaNo){
		Map<String,Object> map=new HashMap<>();
		try {
			QnaReplyVO vo=qService.qnaReplyData(qnaNo);
			map.put("reply", vo);
			return ResponseEntity.ok(map);
		}catch(Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/qna/reply/insert")
	public ResponseEntity<Map<String,Object>> qnaReplyInsert(@RequestBody QnaReplyVO insertData){
		Map<String,Object> map=new HashMap<>();
		try {
			qService.qnaReplyInsert(insertData, insertData.getQnaNo());
		}catch(Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(map);
	}
	
	@PostMapping("/qna/reply/update")
	public ResponseEntity<Map<String,Object>> qnaReplyUpdate(@RequestBody QnaReplyVO updateData){
		Map<String,Object> map=new HashMap<>();
		try {
			qService.qnaReplyUpdate(updateData, updateData.getQnaNo());
		}catch(Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/qna/reply/delete")
	public ResponseEntity<Map<String,Object>> qnaReplyDelete(@RequestParam("no")Integer no,@RequestParam("qnaNo")Integer qnaNo){
		Map<String,Object> map=new HashMap<>();
		try {
			qService.qnaReplyDelete(no, qnaNo);
		}catch(Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(map);
	}
}
