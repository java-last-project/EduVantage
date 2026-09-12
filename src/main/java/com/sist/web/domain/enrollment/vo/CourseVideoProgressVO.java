package com.sist.web.domain.enrollment.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseVideoProgressVO {
    private Integer no,enrollment_no,video_no;
    private Integer currentTime,duration,progress;
    private String completed;
    private LocalDateTime updatedAt;
}
