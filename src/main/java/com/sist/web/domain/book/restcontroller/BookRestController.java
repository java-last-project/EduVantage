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
    @GetMapping("/book/api/find")
    public Map<String, Object> bookFindData(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "category", defaultValue = "전체") String category, 
            @RequestParam(value = "sort", defaultValue = "출간일 순") String sort) {  
        
        Map<String, Object> response = new HashMap<>();
        if (keyword.trim().isEmpty()) {
            return response; 
        }
        
        int rowSize = 12; 
        int start = (page * 12) - 12;
        
        Map<String, Object> dbParam = new HashMap<>();
        dbParam.put("keyword", keyword);
        dbParam.put("category", category); 
        dbParam.put("sort", sort);         
        dbParam.put("start", start);
        
        // DB에서 데이터 및 조건별 검색 총 개수 가져오기
        List<BookVO> list = bService.bookFindData(dbParam);
        int count = bService.bookFindCount(dbParam); 
        
        // 페이지네이션 
        int totalpage = (int) (Math.ceil(count / (double) rowSize));
        if (totalpage == 0) totalpage = 1;
        
        final int BLOCK = 10; 
        int startPage = ((page - 1) / BLOCK) * BLOCK + 1;
        int endPage = ((page - 1) / BLOCK) * BLOCK + BLOCK;
        if (endPage > totalpage) endPage = totalpage;
        
        List<Integer> range = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) range.add(i);
        
        // 프론트로 보낼 데이터
        response.put("list", list);
        response.put("count", count);
        response.put("curpage", page);
        response.put("totalpage", totalpage);
        response.put("startPage", startPage);
        response.put("endPage", endPage);
        response.put("range", range);
        
        return response; 
    }

    
    
}