package com.sist.web.domain.book.restcontroller;

import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.domain.book.service.BookService;
import com.sist.web.domain.book.vo.BookLikeVO;
import com.sist.web.domain.book.vo.BookVO;
import com.sist.web.domain.book.commons.PaginationUtil; 

import jakarta.servlet.http.HttpSession;
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

            // 목록 및 총 데이터 갯수 조회
            List<BookVO> list = bService.bookListData(param);
            int count = bService.bookTotalCount(category);

            // 공통 유틸리티를 통한 페이징 계산
            Map<String, Object> pageInfo = PaginationUtil.getPageInfo(count, page);

            map.put("list", list);
            map.put("count", count);
            map.putAll(pageInfo); 
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(map);
    }
    
    @GetMapping("/book/detail_vue")
    public ResponseEntity<BookVO> book_detail(@RequestParam("no") int no){
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
        
        int start = (page * 12) - 12;
        
        Map<String, Object> dbParam = new HashMap<>();
        dbParam.put("keyword", keyword);
        dbParam.put("category", category); 
        dbParam.put("sort", sort);         
        dbParam.put("start", start);
        
        // DB에서 데이터 및 조건별 검색 총 개수 가져오기
        List<BookVO> list = bService.bookFindData(dbParam);
        int count = bService.bookFindCount(dbParam); 
        
        // 공통 유틸리티를 통한 페이징 계산 
        Map<String, Object> pageInfo = PaginationUtil.getPageInfo(count, page);
        
        // 프론트로 보낼 데이터 구성
        response.put("list", list);
        response.put("count", count);
        response.putAll(pageInfo);
        
        return response; 
    }
    
    // 책 상세페이지 진입 시 상태 확인
    @GetMapping("/book/api/like/status")
    public Map<String, Object> bookLikeStatus(
            @RequestParam("book_no") int bookNo,
            HttpSession session) {

        Integer memberId = (Integer) session.getAttribute("member_id");

        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", bService.bookLikeCount(bookNo));

        boolean isLiked = false;
        if (memberId != null) {
            BookLikeVO vo = new BookLikeVO();
            vo.setBook_no(bookNo);
            vo.setMember_id(memberId);
            isLiked = bService.bookLikeCheck(vo) > 0;
        }
        response.put("isLiked", isLiked);

        return response;
    }

    // 좋아요 버튼 클릭 시 토글 처리
    @PostMapping("/book/api/like/toggle")
    public Map<String, Object> bookLikeToggle(
            @RequestParam("book_no") int bookNo,
            HttpSession session) {

        Integer memberId = (Integer) session.getAttribute("member_id");
        Map<String, Object> response = new HashMap<>();

        if (memberId == null) {
            response.put("error", "로그인이 필요합니다.");
            return response;
        }

        BookLikeVO vo = new BookLikeVO();
        vo.setBook_no(bookNo);
        vo.setMember_id(memberId);

        if (bService.bookLikeCheck(vo) == 0) {
            bService.bookLikeOn(vo);
            response.put("isLiked", true);
        } else {
            bService.bookLikeOff(vo);
            response.put("isLiked", false);
        }
        response.put("likeCount", bService.bookLikeCount(bookNo));

        return response;
    }
}