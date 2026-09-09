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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

            ExamEnrollmentVO vo=new ExamEnrollmentVO();
            vo.setMember_id(mid);
            vo.setTheme(theme!=0?theme:null);
            vo.setExam_no(examNo);
            eService.insertEnrollment(vo);

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
}
