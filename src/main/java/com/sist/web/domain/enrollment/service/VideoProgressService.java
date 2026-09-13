package com.sist.web.domain.enrollment.service;

import com.sist.web.domain.enrollment.vo.CourseVideoProgressVO;

import java.util.List;

public interface VideoProgressService {
    public void saveProgress(CourseVideoProgressVO vo,Integer member_id);
    public List<CourseVideoProgressVO> progressList(Integer enrollment_no,Integer member_id);
}
