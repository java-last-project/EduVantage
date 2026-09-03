package com.sist.web.domain.notification.service;

import com.sist.web.domain.notification.mapper.NotificationMapper;
import com.sist.web.domain.notification.repository.NotificationRepository;
import com.sist.web.domain.notification.vo.NotificationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationMapper notificationMapper;

    //JPQ기반 : 메서드명 길어져서 MyBatis로 변경
    /*
    @Override
    public List<NotificationVO> getIsNotReadNotification(int memberId) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        Date threeDaysAgo = cal.getTime();
        List<NotificationVO> list = notificationRepository.findByMemberIdAndRegdateAfterOrderByRegdateDesc(memberId, threeDaysAgo)
                .stream()
                .map(NotificationVO::from)
                .toList();

        return list;
    }
    */

    @Override
    public List<NotificationVO> findRecentNotifications(int memberId) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        Date targetDate = cal.getTime();
        List<NotificationVO> list = notificationMapper.findRecentNotifications(memberId, targetDate);
        return list;
    }
}
