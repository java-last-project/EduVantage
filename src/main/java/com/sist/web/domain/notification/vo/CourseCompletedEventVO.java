package com.sist.web.domain.notification.vo;

import lombok.Data;

//코스 수료 완료
@Data
public class CourseCompletedEventVO {
    private int member_id;
    private int course_no;
    private String course_title;
    private String tech_stack;
}
