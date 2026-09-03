package com.sist.web.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name="notifications")
@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Notifications {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int no;

    @Column(name="member_id")
    private int memberId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String title;
    private String content;

    //연결URL용 id
    @Column(name="related_id")
    private int relatedId;

    //멱등키
    @Column(name="event_key")
    private String eventKey;

    @Column(name="is_read")
    @Builder.Default
    private String isRead = "N";

    @Enumerated(EnumType.STRING)
    @Column(name="send_status")
    @Builder.Default
    private SendStatus sendStatus = SendStatus.PENDING;

    @Builder.Default
    private Date regdate = new Date();
}
