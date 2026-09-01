package com.sist.web.domain.notification.service;

import com.sist.web.domain.notification.entity.Notifications;
import com.sist.web.domain.notification.repository.NotificationRepository;
import com.sist.web.domain.notification.vo.NotificationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;

    //안읽은 알림 조회
    @Override
    public List<NotificationVO> getIsNotReadNotification() {

        List<NotificationVO> list = notificationRepository.findByIsReadOrderByRegdateDesc("N")
                .stream()
                .map(NotificationVO::from)
                .toList();

        return list;
    }

}
