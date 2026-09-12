package com.sist.web.domain.enrollment.mapper;

import com.sist.web.domain.enrollment.vo.CourseVideoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseVideoMapper {
    public List<CourseVideoVO> courseVideoList(Integer course_no);
    public void courseVideoInsert(CourseVideoVO vo);
}
