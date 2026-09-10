package com.sist.web.domain.book.restcontroller;

import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.domain.book.service.BookService;
import com.sist.web.domain.book.vo.BookCartVO;
import com.sist.web.domain.book.vo.BookCommentVO;
import com.sist.web.domain.book.vo.BookLikeVO;
import com.sist.web.domain.book.vo.BookOrderDetailVO;
import com.sist.web.domain.book.vo.BookOrderVO;
import com.sist.web.domain.book.vo.BookVO;
import com.sist.web.domain.member.service.MemberService;
import com.sist.web.domain.member.vo.MemberVO;
import com.sist.web.domain.book.commons.PaginationUtil; 

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    
    private final BookService bService;
    private final MemberService mService; 
    // 도서 목록
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
    // 도서 상세보기
    @GetMapping("/book/detail_vue")
    public ResponseEntity<BookVO> book_detail(@RequestParam("no") int no){
        BookVO vo = bService.bookDetailData(no);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }
    // 도서 검색
    @GetMapping("/book/find")
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
    @GetMapping("/book/like/status")
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
    @PostMapping("/book/like/toggle")
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
    
    // 장바구니 추가
    @PostMapping("/cart/add")
    public Map<String, String> addCart(@RequestBody BookCartVO vo) {
        Map<String, String> response = new HashMap<>();
        
        try {
            // 이미 장바구니에 담겨있는지 확인
            int count = bService.bookCartCheck(vo);
            
            if (count > 0) {
                // 이미 있으면 수량 증가 업데이트
                bService.bookCartUpdate(vo);
            } else {
                // 없으면 새로 추가
                bService.bookCartInsert(vo);
            }
            
            response.put("status", "success");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
        }
        
        return response;
    }
    
    // 장바구니 목록
    @GetMapping("/cart/list")
    public List<BookCartVO> bookCartList(@RequestParam("member_id") int memberId) {
        return bService.bookCartListData(memberId);
    }
    
    // 주문 사항 저장
    @PostMapping("/order/save")
    public Map<String, String> saveOrder(@RequestBody BookOrderVO orderVO) {
        Map<String, String> map = new HashMap<>();
        
        try {
            bService.bookOrderComplete(orderVO, orderVO.getDetailList());
            
            map.put("status", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "error");
        }
        
        return map;
    }
    
    // 배송지 입력 위한 회원 정보 
    @GetMapping("/member/info_vue")
    public ResponseEntity<MemberVO> getMemberInfo(HttpSession session) {
        int memberId = (int) session.getAttribute("member_id");
        MemberVO vo = mService.memberDetailData(memberId); 
        return ResponseEntity.ok(vo);
    }
    
    // 댓글 목록 불러오기
    @GetMapping("/comment/list")
    public List<BookCommentVO> commentList(int book_no) {
        return bService.bookCommentListData(book_no);
    }

    // 일반 새 댓글 등록
    @PostMapping("/comment/insert")
    public String commentInsert(BookCommentVO vo, HttpSession session) {
        try {
            vo.setMember_id((Integer) session.getAttribute("member_id"));
            vo.setName((String) session.getAttribute("name"));
            
        	bService.bookCommentInsert(vo);
        	
            return "yes"; 
        } catch (Exception e) {
            e.printStackTrace();
            
            return "no";
        }
    }
    
    
}