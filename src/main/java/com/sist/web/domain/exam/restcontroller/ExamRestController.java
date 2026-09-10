package com.sist.web.domain.exam.restcontroller;

import com.sist.web.domain.exam.service.ExamService;
import com.sist.web.domain.exam.vo.ExamEnrollmentVO;
import com.sist.web.domain.exam.vo.ExamQuestionVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ExamRestController {
    private final ExamService eService;

    @PostMapping("/exam/detail_vue")
    public ResponseEntity<Map<String,Object>> exam_detail_vue(@RequestBody Map<String,Object> params, HttpSession session){
        Map<String,Object> map=new HashMap<>();
        try{
            int count=(Integer)params.get("count");
            Integer theme=0;
            if(params.containsKey("theme")){
                theme=(Integer)params.get("theme");
                map.put("theme", theme);
            }
            Integer examNo=(Integer)params.get("examNo");
            Object sessionMid=session.getAttribute("member_id");
            if(sessionMid==null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            int mid=Integer.parseInt(String.valueOf(sessionMid));

            ExamEnrollmentVO vo=eService.getOrCreateEnrollment(mid,examNo,theme);
            map.put("enrollmentNo",vo.getNo());
            map.put("startTime",vo.getStarttime());

            String title=null;
            if(examNo!=null && examNo>0){
                title=eService.getExamTitle(examNo);
            }
            List<ExamQuestionVO> list=eService.examDetailData(examNo,theme,count);
            map.put("enrollmentNo",vo.getNo());
            map.put("title",title);
            map.put("count",count);
            map.put("list",list);
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(map);
    }

    @PostMapping("/exam/submit_vue")
    public ResponseEntity<?> examSubmit(@RequestBody Map<String, Object> params, HttpSession session){
        try{
            Object sessionMid=session.getAttribute("member_id");
            if(sessionMid==null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Map<String,Object> map=eService.submitExam(params);
            return ResponseEntity.ok(map);
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/exam/result_vue")
    public ResponseEntity<?> exam_result_vue(@RequestParam("no") int enrollmentNo, HttpSession session) {
        try {
            Object sessionMid=session.getAttribute("member_id");
            if (sessionMid==null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Map<String, Object> resultData=eService.getExamResultData(enrollmentNo);
            return ResponseEntity.ok(resultData);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
