package com.sist.web.domain.enrollment.service;

import com.sist.web.domain.course.vo.CourseVO;
import com.sist.web.domain.enrollment.vo.CourseVideoVO;
import com.sist.web.domain.enrollment.vo.YoutubeVideoVO;

import java.util.List;

public interface YoutubeService {
    public List<CourseVideoVO> searchVideos(CourseVO course);
}
