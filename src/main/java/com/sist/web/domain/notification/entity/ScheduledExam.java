package com.sist.web.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * NO         NOT NULL NUMBER
 * TITLE      NOT NULL VARCHAR2(1000)
 * OPEN_DATE  NOT NULL TIMESTAMP(6)
 * CLOSE_DATE NOT NULL TIMESTAMP(6)
 */
@Entity
@Table(name="scheduled_exam")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduledExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private String title;
    @Column(name="open_date")
    private Date openDate;
    @Column(name="close_date")
    private Date closeDate;
}
