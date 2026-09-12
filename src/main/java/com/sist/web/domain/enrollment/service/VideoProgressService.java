package com.sist.web.domain.enrollment.service;

import com.sist.web.domain.enrollment.vo.CourseVideoProgressVO;

import java.util.List;

public interface VideoProgressService {
    public void saveProgress(CourseVideoProgressVO vo);
    public List<CourseVideoProgressVO> progressList(Integer enrollment_no);
}
