package com.sist.web.domain.community.restcontroller;

import com.sist.web.domain.community.vo.FreeCommentVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping("/freeboard/comment_vue")
	public ResponseEntity<Map<String,Object>> freeboard_comment_vue(@RequestParam(value="page",defaultValue="1")int page,@RequestParam("board_no")int board_no){
		Map<String,Object> map=new HashMap<>();
		try{
			List<FreeCommentVO> list=fService.freeCommentList(board_no);
			int[] pages= fService.freeCommentCount(board_no,page);
			String[] tags= {"curpage","totalpage","startPage","endPage","count"};
			map.put("list", list);
			for(int i=0;i<pages.length;i++) {
				map.put(tags[i], pages[i]);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(map);
	}
	@PostMapping("/freeboard/comment_insert_vue")
	public ResponseEntity<Map<String,Object>> freeboard_comment_insert(@RequestBody FreeCommentVO insertData){
		Map<String,Object> map=new HashMap<>();
		try{
			Integer id=insertData.getMember_id();
			if(id==null){
				System.out.println("로그인 정보 없음");
			}
			fService.freeBoardCommentInsert(insertData);
		}catch(Exception ex){
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(map);
	}
	@PostMapping("/freeboard/comment_update_vue")
	public ResponseEntity<Map<String,Object>> freeboard_comment_update(@RequestParam("no")int no,@RequestParam("msg")String msg){
		Map<String,Object> map=new HashMap<>();
		try{
			fService.freeBoardCommentUpdate(no,msg);
		}catch(Exception ex){
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(map);
	}
	@GetMapping("/freeboard/comment_delete_vue")
	public ResponseEntity<Map<String,Object>> freeboard_delete_vue(@RequestParam("no")int no){
		Map<String,Object> map=new HashMap<>();
		try{
			fService.freeBoardCommentDelete(no);
		}catch(Exception ex){
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(map);
	}
}
