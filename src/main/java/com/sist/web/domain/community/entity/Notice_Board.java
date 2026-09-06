package com.sist.web.domain.community.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="NOTICE_BOARD")
@Getter@Setter
public class Notice_Board {
    @Id
    private Integer no;
    private Integer member_id,type,hit;
    private String subject,content;
    private LocalDateTime regdate;
}
