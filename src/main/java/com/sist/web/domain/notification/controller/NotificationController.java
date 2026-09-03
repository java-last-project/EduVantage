package com.sist.web.domain.notification.controller;

import com.sist.web.domain.member.mapper.MemberMapper;
import com.sist.web.domain.notification.service.NotificationService;
import com.sist.web.domain.notification.vo.NotificationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
