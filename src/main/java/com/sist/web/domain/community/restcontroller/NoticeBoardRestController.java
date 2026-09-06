package com.sist.web.domain.community.restcontroller;

import com.sist.web.domain.community.entity.Notice_Board;
import com.sist.web.domain.community.service.NoticeBoardService;
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
    public ResponseEntity<Map<String,Object>> notice_list_vue(@RequestParam(value="page",defaultValue="1")int page){
        Map<String,Object> map=new HashMap<>();
        try{
            List<Notice_Board> list=nService.noticeBoardList(page);
            Map<String,Object> pages=nService.noticeBoardPage(page);
            map.put("list",list);
            map.put("pages",pages);
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(map);
    }
}
