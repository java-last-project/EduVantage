package com.sist.web.domain.community.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="NOTICE_BOARD")
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class NoticeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="NO")
    private Integer no;

    @Column(name="MEMBER_ID",nullable=false)
    private Integer memberId;

    @Column(name="CATEGORY_NO",nullable=false)
    private Integer categoryNo;

    @Column(name="HIT")
    private Integer hit=0;

    @Column(name="SUBJECT",nullable=false)
    private String subject;

    @Column(name="CONTENT",nullable=false,columnDefinition="CLOB")
    private String content;

    @Column(name="REGDATE",updatable=false)
    private LocalDateTime regdate;

    @PrePersist
    public void prePersist() {
        this.regdate=LocalDateTime.now();
        if (this.hit==null) {
            this.hit=0;
        }
    }

    public NoticeBoard (Integer memberId,Integer categoryNo,String subject,String content){
        if(categoryNo==null||categoryNo==0){
            throw new IllegalArgumentException("카테고리를 설정해주세요.");
        }
        if(subject==null||subject.trim().isEmpty()){
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        if(content==null||content.trim().isEmpty()){
            throw new IllegalArgumentException("내용은 필수입니다.");
        }
        this.memberId=memberId;
        this.categoryNo=categoryNo;
        this.subject=subject;
        this.content=content;
        this.hit=0;
    }

    public void hitIncrement(){
        this.hit++;
    }

    public void update(Integer categoryNo,String subject,String content){
        if(categoryNo==null||categoryNo==0){
            throw new IllegalArgumentException("카테고리를 설정해주세요.");
        }
        if(subject==null||subject.trim().isEmpty()){
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        if(content==null||content.trim().isEmpty()){
            throw new IllegalArgumentException("내용은 필수입니다.");
        }
        this.categoryNo=categoryNo;
        this.subject=subject;
        this.content=content;
    }
}
