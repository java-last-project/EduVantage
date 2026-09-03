package com.sist.web.domain.book.restcontroller;

import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.domain.book.service.BookService;
import com.sist.web.domain.book.vo.BookVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    
    private final BookService bService;

    @GetMapping("/book/list_vue")
    public ResponseEntity<Map> book_list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "category", defaultValue = "전체") String category,
            @RequestParam(value = "sort", defaultValue = "출간일 순") String sort) {
        
        Map map = new HashMap();
        try {
            int start = (page * 12) - 12; 
            
            Map<String, Object> param = new HashMap<>();
            param.put("start", start);
            param.put("category", category);
            param.put("sort", sort);

            // 목록
            List<BookVO> list = bService.bookListData(param);
            int[] pages = bService.bookTotalPage(page, category);
            int count = bService.bookTotalCount(category);

            map.put("list", list);
            map.put("curpage", pages[0]);
            map.put("totalpage", pages[1]);
            map.put("startPage", pages[2]);
            map.put("endPage", pages[3]);
            map.put("count", count);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(map);
    }
    @GetMapping("/book/detail_vue")
    public ResponseEntity<BookVO>book_detail(@RequestParam("no") int no){
    	BookVO vo = bService.bookDetailData(no);
    	return new ResponseEntity<>(vo, HttpStatus.OK);
    }
}