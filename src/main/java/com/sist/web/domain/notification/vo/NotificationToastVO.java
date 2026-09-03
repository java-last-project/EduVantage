package com.sist.web.domain.notification.vo;

import com.sist.web.domain.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationToastVO {
    private String title;
    private String content;
    private NotificationType type;
}
