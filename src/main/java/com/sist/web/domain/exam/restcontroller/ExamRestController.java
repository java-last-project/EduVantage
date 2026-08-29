package com.sist.web.domain.exam.restcontroller;

import com.sist.web.domain.exam.service.ExamService;
import com.sist.web.domain.exam.vo.ExamQuestionVO;
import lombok.RequiredArgsConstructor;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExamRestController {
    private final ExamService eService;

    @PostMapping("/exam/detail_vue")
    public ResponseEntity<Map<String,Object>> exam_detail_vue(@RequestBody Map<String,Object> params){
        Map<String,Object> map=new HashMap<>();
        try{
            int count=(Integer)params.get("count");
            Integer type=0;
            if(params.containsKey("type")){
                type=(Integer)params.get("type");
            }
            List<ExamQuestionVO> list=eService.examListData(type,count);
            map.put("list",list);
        }catch(Exception ex){
            ex.printStackTrace();
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(map);
    }
}
