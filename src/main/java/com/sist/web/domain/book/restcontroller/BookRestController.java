package com.sist.web.domain.book.restcontroller;

import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    
    private Map<String, Object> getCommentList(int book_no, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("rList", bService.bookCommentListData(book_no));
        map.put("count", bService.bookCommentCount(book_no));
        map.put("curpage", page);
        map.put("totalpage", 1);
        return map;
    }

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

            List<BookVO> list = bService.bookListData(param);
            int count = bService.bookTotalCount(category);

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

    // 도서 상세보기 (이 부분의 int no에 @RequestParam("no")가 빠져있어서 에러가 났던 것입니다!)
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
        
        List<BookVO> list = bService.bookFindData(dbParam);
        int count = bService.bookFindCount(dbParam); 
        
        Map<String, Object> pageInfo = PaginationUtil.getPageInfo(count, page);
        
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
            int count = bService.bookCartCheck(vo);
            if (count > 0) {
                bService.bookCartUpdate(vo);
            } else {
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
        Integer memberId = (Integer) session.getAttribute("member_id");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        MemberVO vo = mService.memberDetailData(memberId);
        return ResponseEntity.ok(vo);
    }
    
    // 댓글 목록 불러오기
    @GetMapping("/comment/list_vue")
    public Map<String, Object> commentListVue(
            @RequestParam("fno") int fno, 
            @RequestParam(value="page", defaultValue="1") int page) {
        return getCommentList(fno, page);
    }

    // 댓글 및 대댓글 등록
    @PostMapping("/comment/insert_vue")
    public Map<String, Object> commentInsertVue(@RequestBody BookCommentVO vo, HttpSession session) {
        String name = (String) session.getAttribute("name");
        vo.setName(name);

        if (vo.getRoot() == 0) {
            bService.bookCommentInsert(vo); // 일반 댓글
        } else {
            bService.bookCommentReplyInsert(vo); // 대댓글
        }
        return getCommentList(vo.getBook_no(), 1);
    }

    // 댓글 수정
    @PutMapping("/comment/update_vue")
    public Map<String, Object> commentUpdateVue(@RequestBody BookCommentVO vo) {
        bService.bookCommentUpdate(vo);
        return getCommentList(vo.getBook_no(), 1);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/delete_vue")
    public Map<String, Object> commentDeleteVue(
            @RequestParam("no") int no,
            @RequestParam("fno") int fno) {
        bService.bookCommentDelete(no);
        return getCommentList(fno, 1);
    }
}