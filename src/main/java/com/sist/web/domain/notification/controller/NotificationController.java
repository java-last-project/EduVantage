package com.sist.web.domain.notification.controller;

import com.sist.web.domain.member.mapper.MemberMapper;
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

    /**
     * 최근 3일 이내 알림 조회
     * @param authentication
     * @return
     */
    @GetMapping
    public ResponseEntity<List<NotificationVO>> getRecentNotifications(Authentication authentication){
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
}
