package com.sist.web.domain.enrollment.vo;

import lombok.Data;

@Data
public class CourseVideoVO {
    private Integer no,course_no,vOrder;
    private String videoId,title,thumbnail;
}
