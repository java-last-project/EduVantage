package com.sist.web.domain.exam.vo;

import lombok.Data;

@Data
public class RecommendCourseVO {
    private Integer course_no;
    private Integer question_no;
    private double similarity;

    // 종합 추천
    private int matched_count;
    private double avg_similarity;
    private double recommendation_score;

    // 강의
    private String title,thumbnail;
}
