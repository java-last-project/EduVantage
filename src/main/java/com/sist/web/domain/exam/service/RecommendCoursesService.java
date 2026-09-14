package com.sist.web.domain.exam.service;

import com.sist.web.domain.exam.vo.RecommendCourseVO;

import java.util.List;

public interface RecommendCoursesService {
    public List<RecommendCourseVO> getRecommentCourses(List<Integer> wrongQuestionNos);
}
