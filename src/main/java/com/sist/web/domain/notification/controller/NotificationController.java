package com.sist.web.domain.notification.controller;

import com.sist.web.domain.member.mapper.MemberMapper;
import com.sist.web.domain.notification.dto.ExamSubscribeRequest;
import com.sist.web.domain.notification.dto.MarkReadRequest;
import com.sist.web.domain.notification.service.NotificationService;
import com.sist.web.domain.notification.vo.NotificationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final MemberMapper memberMapper;

    @GetMapping
    public ResponseEntity<List<NotificationVO>> getIsNotReadNotification(Authentication authentication){
        String username = authentication.getName();
        int memberId = memberMapper.memberInfoData(username).getMember_id();
        List<NotificationVO> result = notificationService.findRecentNotifications(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    /**
     * 모두 읽음으로 표시
     * @param request: 조회된 알림의 no들
     * @return -> read = true로 처리
     */
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@RequestBody MarkReadRequest request){
        notificationService.markAllAsRead(request.getNos());

        return ResponseEntity.ok().build();
    }

    /**
     * 정기 시험 알림 구독
     * @param request: examNo
     * @param authentication: 유저정보
     * @return -> 정기시험알림구독 테이블에 insert
     */
    @PostMapping("exam/subscribe")
    public ResponseEntity<Void> subscribeExam(@RequestBody ExamSubscribeRequest request, Authentication authentication){
        String username = authentication.getName();
        int memberId = memberMapper.memberInfoData(username).getMember_id();

        notificationService.subscribeExam(memberId, request.getExamNo());

        return  ResponseEntity.ok().build();
    }
}
