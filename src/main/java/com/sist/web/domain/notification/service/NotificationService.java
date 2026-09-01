package com.sist.web.domain.notification.service;


import com.sist.web.domain.notification.vo.NotificationVO;

import java.util.List;

public interface NotificationService {
    //안읽음 조회
    List<NotificationVO> getIsNotReadNotification();
}
