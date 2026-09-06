package com.sist.web.domain.notification.controller;


import com.sist.web.domain.exam.vo.ScheduledExamVO;
import com.sist.web.domain.member.mapper.MemberMapper;
import com.sist.web.domain.notification.dto.ExamSubscribeRequest;
import com.sist.web.domain.notification.service.ScheduledExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class ScheduledExamController {
    private final ScheduledExamService scheduledExamService;
    private final MemberMapper memberMapper;

    @GetMapping("/scheduled-exam")
    public ResponseEntity<Page<ScheduledExamVO>> getScheduledExamByMonth(
            Authentication authentication,
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(defaultValue = "0", required = false) int page){

        Integer memberId = null;
        if(authentication != null) {
            String username = authentication.getName();
            memberId = memberMapper.memberInfoData(username).getMember_id();
        }

        Pageable pageable = PageRequest.of(page,3, Sort.by("openDate").ascending());
        Page<ScheduledExamVO> result = scheduledExamService.getExamByMonth(memberId, year, month, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * 정기 시험 알림 구독
     * @param request: examNo
     * @param authentication: 유저정보
     * @return -> 정기시험알림구독 테이블에 insert
     */
    @PostMapping("exam/subscribe")
    public ResponseEntity<?> subscribeExam(@RequestBody ExamSubscribeRequest request, Authentication authentication){
        String username = authentication.getName();
        int memberId = memberMapper.memberInfoData(username).getMember_id();
        try {
            scheduledExamService.subscribeExam(memberId, request.getExamNo());

            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","이미 구독한 시험입니다"));
        }
    }

    @DeleteMapping("/exam/subscribe/{examNo}")
    public ResponseEntity<Void> unsubscribeExam(@PathVariable("examNo") int examNo, Authentication authentication){
        String username = authentication.getName();
        int memberId = memberMapper.memberInfoData(username).getMember_id();

        scheduledExamService.unsubscribeExam(memberId, examNo);
        return ResponseEntity.ok().build();
    }
}
