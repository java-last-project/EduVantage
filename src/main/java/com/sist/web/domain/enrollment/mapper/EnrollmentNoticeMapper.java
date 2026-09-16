package com.sist.web.domain.enrollment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sist.web.domain.enrollment.vo.CourseNoticeVO;

import java.util.*;

@Mapper
@Repository
public interface EnrollmentNoticeMapper {
	public List<CourseNoticeVO> courseNoticeListData(
			@Param("course_id") int course_id,
			@Param("start") int start
			);
	public int courseNoticeRowCount(int course_id);
	public CourseNoticeVO courseNoticeDetailData(int no);
}
