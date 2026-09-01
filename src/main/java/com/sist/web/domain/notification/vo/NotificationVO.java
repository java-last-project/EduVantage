package com.sist.web.domain.notification.vo;



import com.sist.web.domain.notification.entity.Notifications;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class NotificationVO {
    private int no;
    private String type;
    private String title;
    private String content;
    private int related_id;
    private boolean read;
    private Date regdate;

    public static NotificationVO from(Notifications noti){
        return NotificationVO.builder()
                .no(noti.getNo())
                .type(noti.getType())
                .title(noti.getTitle())
                .content(noti.getContent())
                .related_id(noti.getRelated_id())
                .read("Y".equals(noti.getIsRead()))
                .regdate(noti.getRegdate())
                .build();
    }
}
