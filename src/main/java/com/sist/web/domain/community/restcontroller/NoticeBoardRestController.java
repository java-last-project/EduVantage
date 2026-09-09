package com.sist.web.domain.community.restcontroller;

import com.sist.web.domain.community.entity.NoticeBoard;
import com.sist.web.domain.community.service.NoticeBoardService;
import com.sist.web.domain.community.vo.NoticeBoardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class NoticeBoardRestController {
    private final NoticeBoardService nService;

    @GetMapping("/notice/list_vue")
    public ResponseEntity<Map<String,Object>> notice_list_vue(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="fd",required=false)String fd,@RequestParam(value="categoryNo",required=false)Integer categoryNo){
        Map<String,Object> map=new HashMap<>();
        try{
            List<NoticeBoardVO> list=nService.noticeBoardList(page,fd,categoryNo);
            Map<String,Object> pages=nService.noticeBoardPage(page,fd,categoryNo);
            map.put("list",list);
            map.put("curpage",pages.get("curpage"));
            map.put("totalpage",pages.get("totalpage"));
            map.put("startPage",pages.get("startPage"));
            map.put("endPage",pages.get("endPage"));
            map.put("count",pages.get("count"));
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(map);
    }
}
