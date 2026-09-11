package com.sist.web.domain.community.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeBoardVO {
    private Integer no,member_id,hit,category_no;
    private String subject,content,dbday,name,category;
    private LocalDateTime regdate;
}
