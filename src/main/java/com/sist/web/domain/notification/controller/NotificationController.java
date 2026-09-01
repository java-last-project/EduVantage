package com.sist.web.domain.notification.controller;

import com.sist.web.domain.notification.service.NotificationService;
import com.sist.web.domain.notification.vo.NotificationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/notread")
    public ResponseEntity<List<NotificationVO>> getIsNotReadNotification(){
        List<NotificationVO> result = notificationService.getIsNotReadNotification();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}
