package com.sist.web.domain.notification.service;

import com.sist.web.domain.notification.mapper.NotificationMapper;
import com.sist.web.domain.notification.vo.NotificationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{
    private final NotificationMapper notificationMapper;


    @Override
    public List<NotificationVO> findRecentNotifications(int memberId) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        Date targetDate = cal.getTime();
        List<NotificationVO> list = notificationMapper.findRecentNotifications(memberId, targetDate);
        list.forEach(n -> System.out.println("no=" + n.getNo() + ", read=" + n.isRead()));
        return list;
    }

    @Override
    public void markAllAsRead(List<Integer> nos) {
        notificationMapper.markAllAsRead(nos);
    }

    @Override
    public void markAsRead(int no) {
        notificationMapper.markAsRead(no);
    }
}
