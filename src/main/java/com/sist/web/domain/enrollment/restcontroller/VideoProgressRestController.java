package com.sist.web.domain.enrollment.restcontroller;

import com.sist.web.domain.enrollment.service.VideoProgressService;
import com.sist.web.domain.enrollment.vo.CourseVideoProgressVO;
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
    public ResponseEntity<Void> videoProgressSave(@RequestBody CourseVideoProgressVO vo){
        try{
            pService.saveProgress(vo);
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/{enrollment_no}")
    public ResponseEntity<List<CourseVideoProgressVO>> videoProgressList(@PathVariable("enrollment_no")Integer enrollmetn_no){
        try{
            List<CourseVideoProgressVO> list=pService.progressList(enrollmetn_no);
            return ResponseEntity.ok(list);
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
