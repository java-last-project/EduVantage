package com.sist.web.domain.enrollment.restcontroller;

import com.sist.web.domain.enrollment.service.VideoProgressService;
import com.sist.web.domain.enrollment.vo.CourseVideoProgressVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enrollment/progress")
public class VideoProgressRestController {
    private final VideoProgressService pService;

    @PostMapping
    public ResponseEntity<Void> videoProgressSave(@RequestBody CourseVideoProgressVO vo, HttpSession session){
        try{
            // 요청값 대신 session 회원 ID로 접근 권한 검증
            Integer member_id=(Integer)session.getAttribute("member_id");
            pService.saveProgress(vo,member_id);
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/{enrollment_no}")
    public ResponseEntity<List<CourseVideoProgressVO>> videoProgressList(@PathVariable("enrollment_no")Integer enrollment_no,HttpSession session){
        try{
            Integer member_id=(Integer)session.getAttribute("member_id");
            List<CourseVideoProgressVO> list=pService.progressList(enrollment_no,member_id);
            return ResponseEntity.ok(list);
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
