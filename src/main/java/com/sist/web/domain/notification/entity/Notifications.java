package com.sist.web.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name="notifications")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int no;
    @Column(name="member_id")
    private int memberId;
    private String type;
    private String title;
    private String content;
    private int related_id;
    private String event_key;
    @Column(name="is_read")
    private String isRead = "N";

    @Enumerated(EnumType.STRING)
    private SendStatus send_status = SendStatus.PENDING;

    private Date regdate;
}
