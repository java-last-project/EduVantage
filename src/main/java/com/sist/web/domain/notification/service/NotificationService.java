package com.sist.web.domain.notification.service;


import com.sist.web.domain.notification.vo.NotificationVO;

import java.util.Date;
import java.util.List;

public interface NotificationService {
    //List<NotificationVO> getIsNotReadNotification(int memberId);

    List<NotificationVO> findRecentNotifications(int memberId);
}
